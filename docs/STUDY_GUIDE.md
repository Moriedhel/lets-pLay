# Let's Play — Progressive Study Guide

This is a just-in-time syllabus. Expand a module shortly before using it, then
update its status after real implementation and explanation. Do not generate a
large generic textbook unrelated to the repository.

## Status meanings

| Status | Meaning |
|---|---|
| `NOT STARTED` | Not yet assessed or studied here |
| `STUDY NEXT` | Required before the active implementation increment |
| `INTRODUCED` | Explained and recognized in a small example |
| `PRACTISED` | Used and tested in this project |
| `AUDIT READY` | Student can explain it and point to verified evidence |

## Curriculum map

| ID | Module | First needed | Status |
|---|---|---|---|
| S-01 | Java project essentials and Maven | P0/P1 | INTRODUCED |
| S-02 | HTTP, JSON and REST semantics | P0/P4 | NOT STARTED |
| S-03 | Spring Boot, beans and dependency injection | P0/P1 | INTRODUCED |
| S-04 | Spring MVC controllers and serialization | P4 | NOT STARTED |
| S-05 | MongoDB and Spring Data MongoDB | P3 | NOT STARTED |
| S-06 | DTOs, mapping and Bean Validation | P4 | NOT STARTED |
| S-07 | Exceptions and global HTTP error handling | P4 | NOT STARTED |
| S-08 | Passwords, authentication and tokens | P5/P6 | NOT STARTED |
| S-09 | Roles, authorities and ownership authorization | P6/P7 | NOT STARTED |
| S-10 | JUnit, Mockito, MockMvc and integration tests | P1 onward | NOT STARTED |
| S-11 | Git branches, commits, PRs and Gitea Actions | P0/P2 | NOT STARTED |
| S-12 | HTTPS, TLS, certificates and secret handling | P10 | NOT STARTED |

## Required shape of every expanded module

When a module becomes `STUDY NEXT`, add:

1. Plain-language explanation.
2. Why this phase needs it.
3. A small example connected to actual or imminent project code.
4. A useful Go comparison, only if it clarifies the idea.
5. Common mistakes and security implications.
6. Commands or annotations introduced.
7. Links to official documentation.
8. Two or three check questions.
9. A short “show me in this repository” task.

## S-01 — Java project essentials and Maven

Study: packages and imports; classes/interfaces; constructors and access control;
collections/generics; enums; `Optional`; exceptions; annotations; records if used;
Maven `pom.xml`, dependencies, plugins, wrapper and lifecycle; `test`, `package`,
`verify`, `spring-boot:run`.

Readiness check: explain how `pom.xml` differs from and overlaps with `go.mod`, and
run the wrapper commands without relying on a globally installed Maven version.

## S-02 — HTTP, JSON and REST semantics

Study: request/response, headers and bodies; JSON; resource paths; GET/POST/PUT/
PATCH/DELETE; path variables and query parameters; idempotency; 200/201/204/400/
401/403/404/409/500; especially authentication (401) versus permission (403).

Readiness check: design one create, read, update and delete contract with expected
success and failure statuses.

## S-03 — Spring Boot and dependency injection

Study: `@SpringBootApplication`; starters; auto-configuration; application context;
beans; component scanning; inversion of control; constructor injection; roles of
controller/service/repository/configuration packages; appropriate `@Autowired` use.

Readiness check: explain why a service receives a repository rather than calling
`new UserRepository(...)`.

## S-04 — Spring MVC

Study: `@RestController`, `@RequestMapping`, mapping annotations, `@RequestBody`,
`@PathVariable`, `@RequestParam`, `@Valid`, `ResponseEntity`, JSON serialization
and controller responsibility boundaries.

Readiness check: trace an HTTP request from JSON input to DTO to service and back.

## S-05 — MongoDB and Spring Data MongoDB

Study: databases, collections, documents, BSON and `_id`; references versus
embedding; indexes and unique indexes; `@Document`, `@Id`, `@Field`;
`MongoRepository`; derived queries; connection configuration; safe query building;
reproducible persistence testing.

Readiness check: explain how a repository interface gains CRUD behavior without a
handwritten implementation.

## S-06 — DTOs, mapping and validation

Study: persistence document versus request/response/auth DTO; mapping; immutable
responses; `@NotBlank`, `@NotNull`, `@Size`, `@Email`, `@Positive`, `@Valid`;
normalization; why role and `userId` cannot be trusted from a public body.

Readiness check: show structurally why a response DTO cannot leak a password hash.

## S-07 — Exceptions and HTTP errors

Study: checked/unchecked and custom exceptions; expected versus unexpected failure;
`@RestControllerAdvice`; `@ExceptionHandler`; validation errors; consistent safe
error envelopes; logging without secrets.

Readiness check: map duplicate email, missing resource, invalid body, missing token
and forbidden operation to appropriate statuses.

## S-08 — Passwords, authentication and tokens

Study: hashing versus encryption; adaptive hashes and salts; `PasswordEncoder`;
credential verification; bearer tokens; JWT only if selected; signing, validation,
expiry, secret sources, stateless logout limitations and safe 401 responses.

Readiness check: explain why passwords are never decrypted during login.

## S-09 — Authorization

Study: authentication versus authorization; roles and authorities; request-level
versus method-level rules; ownership; `SecurityFilterChain`; `@EnableWebSecurity`,
`@EnableMethodSecurity`, `@PermitAll`, `@PreAuthorize`, `@PostAuthorize`; 401/403.

Readiness check: decide whether an owner and an admin can update the same Product
and identify where each rule is enforced and tested.

## S-10 — Testing

Study: unit/integration/controller/security tests; JUnit 5; assertions; Mockito;
MockMvc; Arrange–Act–Assert; positive and negative paths; test isolation; test data;
reproducible MongoDB approaches compatible with CI.

Readiness check: explain why a service unit test and an HTTP security test catch
different failures.

## S-11 — Git and Gitea CI/CD

Study: working tree/index/commit; short-lived branches; Conventional Commits;
pushes; PRs; CI events and jobs; runners; workflow YAML; caches; secrets; diagnosing
logs; local/CI command parity.

Readiness check: explain the sequence branch -> commit -> push -> CI -> PR -> merge.

## S-12 — HTTPS and transport security

Study: HTTP versus HTTPS; TLS; certificates and trust; private keys; Java keystores;
self-signed local certificates; profiles and environment-based secret configuration;
what HTTPS protects and what it does not.

Readiness check: run the documented local HTTPS procedure without committing a
private key or keystore password.

