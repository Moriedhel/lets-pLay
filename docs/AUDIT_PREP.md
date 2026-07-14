# Let's Play — Audit Preparation and Evidence

This document connects each audit action to reproducible evidence. Update it as
features are verified; do not mark planned behavior as complete.

## Status meanings

- `NOT IMPLEMENTED` — no implementation yet.
- `PARTIAL` — some behavior exists, but required evidence is incomplete.
- `LOCALLY VERIFIED` — specified local automated/manual checks passed.
- `CI VERIFIED` — relevant automated checks also passed in Gitea.
- `AUDIT READY` — reproduction instructions, code, tests and explanation are complete.

## Functional and security audit matrix

| Audit ID | Auditor action/claim | Requirement | Planned phase | Status | Code evidence | Test/manual evidence |
|---|---|---|---|---|---|---|
| AUD-01 | Clone/start application and receive a response | REQ-O01 | P1/P11 | PARTIAL | `LetsPlayApplication.java` | `.\mvnw.cmd verify` — BUILD SUCCESS, 1 test passes; README run instructions pending P11 |
| AUD-02 | Exercise all User CRUD operations | REQ-F01 | P8 | NOT IMPLEMENTED | — | — |
| AUD-03 | Exercise all Product CRUD operations | REQ-F02 | P9 | NOT IMPLEMENTED | — | — |
| AUD-04 | Authenticate with a token | REQ-A01 | P6 | NOT IMPLEMENTED | — | — |
| AUD-05 | Observe admin/user/ownership restrictions | REQ-A02/A03 | P7–P9 | NOT IMPLEMENTED | — | — |
| AUD-06 | Access Product GET anonymously | REQ-F04 | P7/P9 | NOT IMPLEMENTED | — | — |
| AUD-07 | Update/delete/read a missing resource safely | REQ-E01/E02 | P8/P9 | NOT IMPLEMENTED | — | — |
| AUD-08 | Submit invalid/malformed input and receive suitable 4XX | REQ-S02/E01 | P4 onward | NOT IMPLEMENTED | — | — |
| AUD-09 | Confirm stored passwords are encoded | REQ-S01 | P5 | NOT IMPLEMENTED | — | — |
| AUD-10 | Confirm password/hash is absent from responses/logs | REQ-S03 | P4/P5 | NOT IMPLEMENTED | — | — |
| AUD-11 | Review MongoDB query/input safety | REQ-S04 | P3/P10 | NOT IMPLEMENTED | — | — |
| AUD-12 | Run and call the application over HTTPS | REQ-S05 | P10 | NOT IMPLEMENTED | — | — |
| AUD-13 | Verify required data/controller/validation annotations | REQ-Q01/Q02/Q04 | P3/P4/P8/P9 | NOT IMPLEMENTED | — | — |
| AUD-14 | Verify `@Autowired` is used correctly | REQ-Q05 | P3 onward | NOT IMPLEMENTED | — | — |
| AUD-15 | Verify security annotations and their behavior | REQ-Q03 | P7 | NOT IMPLEMENTED | — | — |
| AUD-16 | Confirm Gitea CI checks meaningful increments | REQ-O02 | P2 onward | NOT IMPLEMENTED | — | — |

## Authorization matrix

Complete only after decision D-05 is approved.

| Operation | Anonymous | User | Resource owner | Admin |
|---|---:|---:|---:|---:|
| Register | TBD | TBD | N/A | TBD |
| Login | TBD | TBD | N/A | TBD |
| List/get Products | Required: allow | Required: allow | Required: allow | Required: allow |
| Create Product | Deny | TBD | TBD | TBD |
| Update/delete Product | Deny | TBD | TBD | TBD |
| List/get Users | Deny | TBD | TBD | TBD |
| Update/delete User | Deny | TBD | TBD | TBD |

## Endpoint contract

Complete after decision D-04. Do not let controller code become the accidental
source of truth.

| Method | Path | Purpose | Public? | Role/owner | Success | Important failures |
|---|---|---|---|---|---|---|
| TBD | TBD | TBD | TBD | TBD | TBD | TBD |

## Annotation evidence

| Annotation | Intended meaningful use | Source file | Verification | Status |
|---|---|---|---|---|
| `@Document` | MongoDB persistence document | TBD | Repository test/review | NOT IMPLEMENTED |
| `@Id` | MongoDB identity | TBD | Repository test/review | NOT IMPLEMENTED |
| `@Field` | Explicit field mapping where justified | TBD | Repository test/review | NOT IMPLEMENTED |
| `@RestController` | REST controller | TBD | HTTP test | NOT IMPLEMENTED |
| `@RequestMapping` | Resource base path | TBD | HTTP test | NOT IMPLEMENTED |
| `@GetMapping` | Read operation | TBD | HTTP test | NOT IMPLEMENTED |
| `@PostMapping` | Create/login operation | TBD | HTTP test | NOT IMPLEMENTED |
| `@PutMapping` | Approved update semantics | TBD | HTTP test | NOT IMPLEMENTED |
| `@DeleteMapping` | Delete operation | TBD | HTTP test | NOT IMPLEMENTED |
| `@Autowired` | Explicit constructor injection evidence | TBD | Context/unit test | NOT IMPLEMENTED |
| `@EnableWebSecurity` | Web security configuration | TBD | Security test | NOT IMPLEMENTED |
| `@EnableMethodSecurity` | Method rules enabled | TBD | Security test | NOT IMPLEMENTED |
| `@PermitAll` | Meaningful anonymous method | TBD | Anonymous test | NOT IMPLEMENTED |
| `@PreAuthorize` | Rule checked before method execution | TBD | Role/owner test | NOT IMPLEMENTED |
| `@PostAuthorize` | Safe single-result rule checked before return | TBD | Owner/non-owner test | NOT IMPLEMENTED |
| Validation annotations | DTO constraints | TBD | Invalid-body tests | NOT IMPLEMENTED |

## Manual scenario register

For each scenario eventually record prerequisites, exact curl/Postman request,
expected status/body, actual result and date verified.

| Scenario | Expected category | Planned phase | Status |
|---|---|---|---|
| Successful registration | 2XX | P5/P6 | NOT IMPLEMENTED |
| Duplicate registration | 409 or approved alternative | P5 | NOT IMPLEMENTED |
| Invalid registration | 400 | P4/P5 | NOT IMPLEMENTED |
| Successful login | 200 + token response | P6 | NOT IMPLEMENTED |
| Incorrect password | 401 | P6 | NOT IMPLEMENTED |
| Missing token on protected route | 401 | P6/P7 | NOT IMPLEMENTED |
| Malformed/invalid token | 401 | P6 | NOT IMPLEMENTED |
| Expired token | 401 | P6 | NOT IMPLEMENTED |
| User attempts admin-only action | 403 | P7/P8 | NOT IMPLEMENTED |
| Anonymous Product GET | 200 | P7/P9 | NOT IMPLEMENTED |
| Create Product | 201 | P9 | NOT IMPLEMENTED |
| Owner updates Product | 200 | P9 | NOT IMPLEMENTED |
| Non-owner updates Product | 403 or approved concealment policy | P9 | NOT IMPLEMENTED |
| Read/update/delete missing resource | 404 | P8/P9 | NOT IMPLEMENTED |
| Password fields absent in responses | no password/hash field | P4/P5/P8 | NOT IMPLEMENTED |
| HTTPS request | successful trusted/explicit local TLS call | P10 | NOT IMPLEMENTED |

## Audit explanation template

For each verified item, be prepared to answer:

1. What requirement does this satisfy?
2. Where does the request enter the application?
3. Which controller, service, repository or security component handles it?
4. Why was this design chosen?
5. What happens on invalid input, missing authentication and insufficient access?
6. Which automated test proves it?
7. What security mistake would occur if this layer or DTO were removed?

