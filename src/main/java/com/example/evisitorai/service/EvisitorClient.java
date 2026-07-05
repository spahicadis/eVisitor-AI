package com.example.evisitorai.service;

import com.example.evisitorai.dto.EvisitorCheckInPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EvisitorClient {

    private static final String LOGIN_PATH = "/Resources/AspNetFormsAuth/Authentication/Login";
    private static final String CHECK_IN_PATH = "/Rest/Htz/CheckInTourist/";

    private final RestClient evisitorRestClient;
    private final ObjectMapper objectMapper;
    private final String username;
    private final String password;

    public EvisitorClient(RestClient evisitorRestClient,
                          ObjectMapper objectMapper,
                          @Value("${evisitor.username}") String username,
                          @Value("${evisitor.password}") String password) {
        this.evisitorRestClient = evisitorRestClient;
        this.objectMapper = objectMapper;
        this.username = username;
        this.password = password;
    }

    public void checkIn(EvisitorCheckInPayload payload) {
        String cookieHeader = login();

        ResponseEntity<String> response;
        try {
            response = this.evisitorRestClient.post()
                    .uri(CHECK_IN_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, cookieHeader)
                    .body(payload)
                    .retrieve()
                    .onStatus(status -> true, (request, res) -> { })
                    .toEntity(String.class);
        } catch (RestClientException e) {
            throw new EvisitorException("Poziv CheckInTourist nije uspio: " + e.getMessage(), e);
        }

        String body = response.getBody() == null ? "" : response.getBody().trim();

        if (!response.getStatusCode().is2xxSuccessful() || !body.isEmpty()) {
            throw new EvisitorException(describeError(body));
        }
    }

    /** Prijava na Authentication service; vraća spojeni Cookie header za poziv CheckInTourist. */
    private String login() {
        Map<String, Object> loginBody = new LinkedHashMap<>();
        loginBody.put("userName", this.username);
        loginBody.put("password", this.password);
        loginBody.put("persistCookie", false);
        ResponseEntity<String> response;
        try {
            response = this.evisitorRestClient.post()
                    .uri(LOGIN_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(loginBody)
                    .retrieve()
                    .onStatus(status -> true, (request, res) -> { })
                    .toEntity(String.class);
        } catch (RestClientException e) {
            throw new EvisitorException("Ne mogu se spojiti na eVisitor: " + e.getMessage(), e);
        }

        String body = response.getBody() == null ? "" : response.getBody().trim();
        List<String> setCookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);

        if (!response.getStatusCode().is2xxSuccessful()
                || !"true".equalsIgnoreCase(body)
                || setCookies == null || setCookies.isEmpty()) {
            throw new EvisitorException("Prijava na eVisitor nije uspjela: " + describeError(body));
        }

        return setCookies.stream()
                .map(cookie -> cookie.split(";", 2)[0])
                .collect(Collectors.joining("; "));
    }

    /** Ljudski čitljiva poruka iz eVisitor responsa - UserMessage, SystemMessage . */
    private String describeError(String body) {
        if (body == null || body.isBlank()) {
            return "eVisitor je vratio grešku bez opisa.";
        }
        if ("false".equalsIgnoreCase(body)) {
            return "neispravno korisničko ime ili lozinka.";
        }
        try {
            JsonNode node = this.objectMapper.readTree(body);
            JsonNode userMessage = node.path("UserMessage");
            JsonNode systemMessage = node.path("SystemMessage");
            if (userMessage.isTextual() && !userMessage.asText().isBlank()) {
                return userMessage.asText();
            }
            if (systemMessage.isTextual() && !systemMessage.asText().isBlank()) {
                return systemMessage.asText();
            }
        } catch (RuntimeException ignored) {
            // nije JSON – vraća se sirovi tekst
        }
        return body;
    }
}
