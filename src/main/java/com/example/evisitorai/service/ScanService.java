package com.example.evisitorai.service;

import com.example.evisitorai.dto.Scan;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScanService {

    private static final String SYSTEM_PROMPT = """
            You extract data from a photo of a travel document (passport or ID card) for tourist check-in.
            Return ONLY values that are clearly visible and legible. If a value is not clearly present
            on the document, return null. NEVER guess, infer or invent a value — prefer null over a guess.

            Document layout hints:
            - Machine Readable Zone (MRZ): if the document has an MRZ (the 2-3 monospaced lines of
              capital letters, digits and "<" at the bottom), PREFER it as the source of truth for
              surname and given names (separated by "<<"), nationality (ISO 3166-1 alpha-3),
              date of birth (YYMMDD) and sex (M/F). When one of THESE fields is unclear or disagrees
              with the MRZ, use the MRZ value.
            - documentNumber is the ONE EXCEPTION to the MRZ rule. Read documentNumber ONLY from the
              printed field in the TOP-RIGHT corner of the document. NEVER take documentNumber from the
              MRZ, from any number printed at the BOTTOM of the document, or from a personal/ID number
              elsewhere on the card. If you cannot clearly read the number in the top-right corner,
              return null — do NOT substitute any other number. The number below is always wrong; ignore it.
            - Personal names: the SURNAME comes FIRST in the data, the given name(s) come AFTER it.
              Map the surname to "touristSurname" and the given name(s) to "touristName". Do not swap them.
            - Passports usually show a "place of birth" (a city, sometimes with a country).
              Use it: derive countryOfBirth from the place-of-birth country when present.
            - countryOfResidence: ALWAYS set it to the SAME value as citizenship (nationality).
            - cityOfResidence: ALWAYS set it to the place-of-birth city (it is essentially always shown).

            Return a JSON object with exactly these keys:
            documentType, documentNumber, touristName, touristSurname, gender, dateOfBirth,
            countryOfBirth, citizenship, countryOfResidence, cityOfResidence.

            Rules:
            - documentType: "PASSPORT_FOREIGN" for a passport, "IDENTITY_CARD_FOREIGN" for an ID card.
            - gender: "MALE" or "FEMALE".
            - dateOfBirth: ISO format YYYY-MM-DD.
            - countryOfBirth, citizenship: ISO 3166-1 alpha-3 codes (e.g. HRV, DEU, ITA).
            - countryOfResidence: MUST equal citizenship (the same ISO3 code). Do not return null.
            - cityOfResidence: the place-of-birth city shown on the document. Do not return null.
            """;

    private final RestClient qwenRestClient;
    private final ObjectMapper objectMapper;
    private final String model;

    public ScanService(RestClient qwenRestClient,
                       ObjectMapper objectMapper,
                       @Value("${qwen.model}") String model) {
        this.qwenRestClient = qwenRestClient;
        this.objectMapper = objectMapper;
        this.model = model;
    }

    public Scan scan(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new ScanException("Nije priložena slika.", null);
        }

        try {
            String dataUrl = toDataUrl(image);

            ChatRequest request = new ChatRequest(
                    this.model,
                    List.of(
                            new ChatMessage("system", SYSTEM_PROMPT),
                            new ChatMessage("user", List.of(
                                    Map.of("type", "text",
                                            "text", "Extract the document data as strict JSON."),
                                    Map.of("type", "image_url",
                                            "image_url", Map.of("url", dataUrl))
                            ))
                    ),
                    0.0,
                    buildJsonSchemaFormat()
            );

            ChatResponse response = this.qwenRestClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(ChatResponse.class);

            String json = extractContent(response);
            return this.objectMapper.readValue(json, Scan.class);

        } catch (ScanException e) {
            throw e;
        } catch (Exception e) {
            throw new ScanException("Skeniranje nije uspjelo: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> buildJsonSchemaFormat() {
        List<String> fields = List.of(
                "documentType", "documentNumber", "touristName", "touristSurname",
                "gender", "dateOfBirth", "countryOfBirth", "citizenship",
                "countryOfResidence", "cityOfResidence");

        Map<String, Object> stringOrNull = Map.of("type", List.of("string", "null"));

        Map<String, Object> properties = new LinkedHashMap<>();
        for (String field : fields) {
            properties.put(field, stringOrNull);
        }

        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        schema.put("properties", properties);
        schema.put("required", fields);
        schema.put("additionalProperties", false);

        Map<String, Object> jsonSchema = new LinkedHashMap<>();
        jsonSchema.put("name", "passport_scan");
        jsonSchema.put("strict", true);
        jsonSchema.put("schema", schema);

        Map<String, Object> responseFormat = new LinkedHashMap<>();
        responseFormat.put("type", "json_schema");
        responseFormat.put("json_schema", jsonSchema);
        return responseFormat;
    }

    private String toDataUrl(MultipartFile image) throws Exception {
        String contentType = image.getContentType() != null ? image.getContentType() : "image/jpeg";
        String base64 = Base64.getEncoder().encodeToString(image.getBytes());
        return "data:" + contentType + ";base64," + base64;
    }

    private String extractContent(ChatResponse response) {
        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            throw new ScanException("Qwen nije vratio odgovor.", null);
        }
        String content = response.choices().get(0).message().content();
        if (content == null || content.isBlank()) {
            throw new ScanException("Qwen je vratio prazan odgovor.", null);
        }

        String trimmed = content.trim();
        if (trimmed.startsWith("```")) {
            trimmed = trimmed.replaceAll("^```(?:json)?", "").replaceAll("```$", "").trim();
        }
        return trimmed;
    }

    private record ChatRequest(String model,
                               List<ChatMessage> messages,
                               double temperature,
                               Map<String, Object> response_format) {
    }

    private record ChatMessage(String role, Object content) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ChatResponse(List<Choice> choices) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        private record Choice(Message message) {
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private record Message(String content) {
        }
    }
}
