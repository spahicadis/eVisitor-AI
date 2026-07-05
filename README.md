# eVisitor-AI

Spring Boot + Thymeleaf aplikacija za prijavu turista: skeniranje isprave lokalnim
AI modelom (autofill forme), spremanje u PostgreSQL i slanje prijave u eVisitor Web API.

## Preduvjeti

- **Java 17+**
- **PostgreSQL** (lokalno, na `localhost:5432`)
- **Lokalni AI model** s OpenAI-compatible API-jem (npr. Qwen3-VL preko LM Studija) — za skeniranje isprava

## 1. Baza

Kreiraj praznu bazu (naziv i kredencijali moraju odgovarati `application.properties`):

```sql
CREATE DATABASE "evisitor-ai";
```

Shemu ne treba ručno raditi — `spring.jpa.hibernate.ddl-auto=update` je kreira pri prvom pokretanju,
a seeder ubaci demo objekte (facilities). Prilagoditi seeder prema vlastitim podacima!

## 2. Procesne env varijable (OBAVEZNO)

eVisitor kredencijali se **ne drže u kodu** nego u env varijablama procesa. Bez njih se aplikacija
**neće pokrenuti** (fail-fast):

| Varijabla | Opis |
|---|---|
| `EVISITOR_USERNAME` | korisničko ime za eVisitor Web API |
| `EVISITOR_PASSWORD` | lozinka za eVisitor Web API |

Za lokalni razvoj dovoljne su **dummy vrijednosti** (npr. `dummy`/`dummy`) — sve osim samog
slanja u eVisitor radi normalno. Za **stvarno** slanje turista u eVisitor trebaju **pravi
pristupni podaci**, koje se traži kod nadležne turističke zajednice.

## 3. `application.properties` (struktura)

```properties
# Baza
spring.datasource.url=jdbc:postgresql://localhost:5432/evisitor-ai
spring.datasource.username=<db-user>
spring.datasource.password=<db-pass>
spring.jpa.hibernate.ddl-auto=update

# Upload slike isprave
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Lokalni AI model (OpenAI-compatible API)
qwen.base-url=http://localhost:1234/v1
qwen.model=qwen/qwen3-vl-8b
qwen.timeout-seconds=120

# eVisitor Web API (kredencijali dolaze iz env varijabli)
evisitor.base-url=https://www.evisitor.hr/eVisitorRhetos_API
evisitor.username=${EVISITOR_USERNAME}
evisitor.password=${EVISITOR_PASSWORD}
evisitor.timeout-seconds=30
```

## 4. AI model (skeniranje isprave)

Skeniranje koristi bilo koji **OpenAI-compatible** `/chat/completions` endpoint s vision podrškom
(testirano s Qwen3-VL preko LM Studija). Za drugi model/servis samo promijeniti:

- `qwen.base-url` — URL API-ja (npr. LM Studio: `http://localhost:1234/v1`)
- `qwen.model` — identifikator učitanog modela

Ako AI model nije dostupan, aplikacija i dalje radi — polja se unesu ručno.

## 5. Pokretanje

```bash
export EVISITOR_USERNAME=dummy
export EVISITOR_PASSWORD=dummy
./mvnw spring-boot:run
```

Aplikacija je na **http://localhost:8080**:

- `/` — Spremi turiste (skeniranje + check-in)
- `/tourists` — Moji turisti (pregled, slanje u eVisitor, uredi/obriši)
- `/facilities` — Moji objekti
