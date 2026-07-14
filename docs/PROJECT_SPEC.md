# Let's Play — Project Specification

This document preserves the exercise requirements and gives each one a stable ID.
It says **what** must be true. Sequencing belongs in `../PLAN.md`; working rules
belong in `../AGENTS.md`.

## Domain model

### User

| Field | Exercise type | Notes to resolve during design |
|---|---|---|
| `id` | `String` | MongoDB identifier |
| `name` | `String` | Validation limits must be chosen |
| `email` | `String` | Normalize and enforce uniqueness |
| `password` | `String` | Persistence contains only an encoded value |
| `role` | `String` | API may use an enum internally; public clients cannot grant admin |

### Product

| Field | Exercise type | Notes to resolve during design |
|---|---|---|
| `id` | `String` | MongoDB identifier |
| `name` | `String` | Validation limits must be chosen |
| `description` | `String` | Validation limits must be chosen |
| `price` | `Double` | Exercise type retained; positive-value validation required |
| `userId` | `String` | Ownership must come from trusted authentication context on writes |

## Functional requirements

| ID | Requirement | Verification summary |
|---|---|---|
| REQ-F01 | Provide RESTful User Create, Read, Update and Delete operations. | Automated controller/service tests plus manual API scenarios |
| REQ-F02 | Provide RESTful Product Create, Read, Update and Delete operations. | Automated controller/service tests plus manual API scenarios |
| REQ-F03 | Use REST resource conventions and appropriate methods/status codes. | Endpoint contract and HTTP tests |
| REQ-F04 | Product GET endpoint(s) are accessible without authentication. | Anonymous HTTP tests and manual request |

## Authentication and authorization requirements

| ID | Requirement | Verification summary |
|---|---|---|
| REQ-A01 | Implement token-based authentication. | Login/token validation tests |
| REQ-A02 | Support `admin` and `user` roles. | Role mapping and authorization tests |
| REQ-A03 | Control protected API access by authentication, role and approved ownership rules. | 401, 403, owner/non-owner/admin tests |

Public product reads are an explicit exception to protected API access. A usable
token system also normally requires public registration and/or login endpoints;
their exact exposure is an open design decision that must be approved.

## Error and operational requirements

| ID | Requirement | Verification summary |
|---|---|---|
| REQ-E01 | Handle expected exceptions with suitable status codes and safe messages. | Validation, conflict, not-found, authentication and authorization tests |
| REQ-E02 | Avoid unhandled failures during expected client-error scenarios. | Negative-path suite and safe fallback review |
| REQ-O01 | Application can be started and called by an auditor. | README procedure executed from a clean setup |
| REQ-O02 | Gitea CI verifies every meaningful increment. | Passing push/PR/main workflows |

“No 5XX errors” does not authorize returning 200 for failures. Expected invalid
client operations must become documented 4XX responses. A genuinely unexpected
server defect should be logged safely and returned through a non-sensitive error
response while the defect is fixed.

## Security requirements

| ID | Requirement | Verification summary |
|---|---|---|
| REQ-S01 | Hash and salt passwords before storage using an appropriate adaptive password encoder. | Persistence test and database/manual inspection |
| REQ-S02 | Validate input and reject malformed or unsafe values. | Bean Validation and negative HTTP tests |
| REQ-S03 | Never return or log passwords, hashes, tokens or other sensitive information. | DTO/serialization tests and log review |
| REQ-S04 | Prevent MongoDB injection by avoiding raw client-built query documents and constraining inputs. | Repository/query review and malicious-input tests |
| REQ-S05 | Use HTTPS to protect data in transit and document reproducible local testing. | Local HTTPS procedure and manual evidence |

## Annotation and code-quality evidence

| ID | Audit expectation |
|---|---|
| REQ-Q01 | Use `@Document`, `@Id` and `@Field` correctly in persistence documents. |
| REQ-Q02 | Use `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping` and `@DeleteMapping` correctly in controllers. |
| REQ-Q03 | Use `@EnableWebSecurity`, `@EnableMethodSecurity`, `@PermitAll`, `@PreAuthorize` and `@PostAuthorize` correctly and meaningfully. |
| REQ-Q04 | Use `@NotNull`/`@NotBlank`, `@Size`, `@Email`, `@Positive` or appropriate validation equivalents. |
| REQ-Q05 | Demonstrate correct `@Autowired` injection while explaining why constructor injection is preferred. |

The goal is evidence of correct behavior, not annotation collection. Each security
annotation must have a meaningful location and a verification scenario. An
appropriate constructor can carry `@Autowired`; field injection should be avoided.

## Testing expectations

Tests must cover:

- Correct User and Product CRUD behavior.
- Authentication and token failures.
- Role and ownership authorization.
- Anonymous Product GET.
- Invalid bodies, fields and identifiers.
- Duplicate email and missing resources.
- Safe error codes/messages for expected failures.
- Password encoding and absence of password data in API responses.
- Reproducible persistence behavior in local development and CI.

CI must not rely on a MongoDB server that was manually installed on one developer's
machine. The testing approach must be chosen after inspecting Docker and runner
capabilities.

## Auditor startup expectation

An auditor must be able to clone the repository and start it from a documented
editor workflow or script, then test it with Postman, curl or a similar client.

The finished `README.md` must document prerequisites, environment variables,
MongoDB, build/test/run commands, HTTPS, authentication, endpoints, roles, errors,
manual examples and the command equivalent to CI.

## Bonus requirements

| ID | Requirement | Constraint |
|---|---|---|
| BONUS-01 | Configure an appropriate CORS policy. | Only after mandatory verification |
| BONUS-02 | Add rate limiting to reduce brute-force/misuse risk. | Only after mandatory verification |

## Open interpretation questions

These must be proposed and approved during the named decision gates in `PLAN.md`:

- Are registration and login public, and who may create users after bootstrap?
- Can a normal user read/update/delete only their own account?
- Can admins perform every User operation?
- Can users create and modify only their own Products?
- Are both collection and single Product GET endpoints public?
- Is update full replacement (`PUT`), partial update (`PATCH`) or both?
- What happens to Products when their owning User is deleted?
- How is the initial admin created without allowing public admin registration?
- Which token format and expiry policy will be used?
- Which local HTTPS profile is practical for auditors?

