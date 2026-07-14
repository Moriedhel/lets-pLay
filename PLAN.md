# Let's Play — Connected Execution Plan

## How this plan works

This file coordinates the project; it does not repeat the full exercise or teach
every concept. Its phases link the other documents:

| Question | Source of truth |
|---|---|
| What must be built? | [`docs/PROJECT_SPEC.md`](docs/PROJECT_SPEC.md) |
| How must Codex work? | [`AGENTS.md`](AGENTS.md) |
| What should I study? | [`docs/STUDY_GUIDE.md`](docs/STUDY_GUIDE.md) |
| What did we actually do and why? | [`docs/LEARNING_LOG.md`](docs/LEARNING_LOG.md) |
| How will I prove it in the audit? | [`docs/AUDIT_PREP.md`](docs/AUDIT_PREP.md) |
| How does an auditor run the finished app? | `README.md` |

Every phase must connect four things:

```text
Requirement ID -> Study module -> Code and test -> Audit evidence
```

## Current state

- Active phase: **P1 complete — Minimal Spring Boot project builds and starts**
- Active increment: **P2 — Gitea CI**
- Approved architecture: Spring Boot 4.1.0 + Maven wrapper, package `com.letsplay`
- Approved authorization matrix: **Not yet decided**
- Approved token approach: **Not yet decided**
- Approved MongoDB test approach: **Not yet decided**
- Bonus work allowed: **No**

Update this section only after the student approves a phase transition.

## Decision gates

These decisions affect later phases and must not be invented silently.

| ID | Decision | Needed before | Status |
|---|---|---|---|
| D-01 | Java 21 (Temurin), Spring Boot 4.1.0, Maven with wrapper | P1 | **RESOLVED** |
| D-02 | Group `com.letsplay`, artifact `lets-play`, local run via `mvnw.cmd spring-boot:run` | P1 | **RESOLVED** |
| D-03 | Reproducible MongoDB testing strategy supported locally and in Gitea CI | P3 | OPEN |
| D-04 | Endpoint contract, PUT/PATCH policy and response shapes | P4 | OPEN |
| D-05 | Authorization and ownership matrix | P6 | OPEN |
| D-06 | Token format, signing strategy, expiry and secret source | P6 | OPEN |
| D-07 | Local HTTPS profile and certificate-generation procedure | P10 | OPEN |
| D-08 | User deletion/product ownership behavior | P8 | OPEN |

Record an approved decision in the relevant learning-log entry and update the
corresponding audit table. Do not create a separate architecture system unless
the project becomes complex enough to need one.

## Branch model

Use:

```text
short-lived branch -> small commits -> push -> CI -> PR -> approved merge -> main CI
```

Suggested branches are listed per phase. A phase may contain several small pushes.
Do not create a new branch for every push.

## Roadmap overview

| Phase | Outcome | Depends on | Primary requirements | Study modules |
|---|---|---|---|---|
| P0 | Repository facts and approved design decisions | None | All (analysis only) | S-01, S-02, S-03, S-11 |
| P1 | Minimal Spring Boot project builds and starts | P0, D-01, D-02 | REQ-O01 | S-01, S-03, S-10 |
| P2 | Gitea CI runs the same verification as local development | P1 | REQ-O02 | S-01, S-10, S-11 |
| P3 | MongoDB documents and repositories are tested reproducibly | P2, D-03 | REQ-F01, REQ-F02, REQ-Q01 | S-05, S-10 |
| P4 | API contracts, DTOs, validation and error envelope exist | P3, D-04 | REQ-F03, REQ-S02, REQ-E01 | S-02, S-04, S-06, S-07 |
| P5 | User creation stores only encoded passwords | P4 | REQ-S01, REQ-S03, REQ-S04 | S-06, S-08, S-10 |
| P6 | Login and token authentication work | P5, D-05, D-06 | REQ-A01, REQ-A02, REQ-A03 | S-08, S-09 |
| P7 | Security policy, roles and public product reads are enforced | P6 | REQ-A02, REQ-A03, REQ-F04, REQ-Q03 | S-09, S-10 |
| P8 | Secured User CRUD is complete | P7, D-08 | REQ-F01, REQ-E01 | S-04, S-07, S-09 |
| P9 | Product CRUD and ownership rules are complete | P8 | REQ-F02, REQ-F04, REQ-E01 | S-04, S-05, S-09 |
| P10 | HTTPS and security hardening are reproducible | P9, D-07 | REQ-S02–S05 | S-12 |
| P11 | Full audit suite and handoff documentation are verified | P10 | All mandatory | All relevant |
| B1 | CORS policy | P11 | BONUS-01 | S-09 |
| B2 | Authentication rate limiting | P11 | BONUS-02 | S-08, S-09 |

## Requirement traceability

This table prevents a requirement from disappearing between the exercise, a
phase, the curriculum and the audit. “Primary phase” is where the requirement is
completed; earlier phases may prepare supporting code.

| Requirement | Primary phase | Study modules | Audit evidence |
|---|---|---|---|
| REQ-F01 User CRUD | P8 | S-02, S-04, S-06, S-07, S-09 | AUD-02, AUD-07, AUD-08 |
| REQ-F02 Product CRUD | P9 | S-02, S-04, S-05, S-06, S-07, S-09 | AUD-03, AUD-07, AUD-08 |
| REQ-F03 REST conventions | P4, then P8/P9 | S-02, S-04 | AUD-02, AUD-03 |
| REQ-F04 Public Product GET | P7/P9 | S-02, S-09, S-10 | AUD-06 |
| REQ-A01 Token authentication | P6 | S-08, S-10 | AUD-04 |
| REQ-A02 Roles | P6/P7 | S-09, S-10 | AUD-05 |
| REQ-A03 Protected access | P7–P9 | S-09, S-10 | AUD-05 |
| REQ-E01 Expected error handling | P4, then P8/P9 | S-07, S-10 | AUD-07, AUD-08 |
| REQ-E02 No unhandled expected failures | P11 | S-07, S-10 | AUD-07, AUD-08 |
| REQ-O01 Auditor can run app | P11 | S-01, S-03, S-12 | AUD-01, AUD-12 |
| REQ-O02 CI per meaningful increment | P2 onward | S-10, S-11 | AUD-16 |
| REQ-S01 Password encoding | P5 | S-08, S-10 | AUD-09 |
| REQ-S02 Input validation | P4, then P8/P9 | S-06, S-07, S-10 | AUD-08 |
| REQ-S03 Sensitive-data protection | P4/P5, final P11 | S-06, S-08, S-10 | AUD-10 |
| REQ-S04 MongoDB injection prevention | P3, final P10 | S-05, S-06, S-10 | AUD-11 |
| REQ-S05 HTTPS | P10 | S-12 | AUD-12 |
| REQ-Q01 Data annotations | P3 | S-05 | AUD-13 |
| REQ-Q02 Controller annotations | P8/P9 | S-04 | AUD-13 |
| REQ-Q03 Security annotations | P7 | S-09 | AUD-15 |
| REQ-Q04 Validation annotations | P4 | S-06 | AUD-13 |
| REQ-Q05 `@Autowired` evidence | First appropriate injected constructor, final P11 | S-03 | AUD-14 |
| BONUS-01 CORS | B1 | S-09 | Bonus audit entry added only if implemented |
| BONUS-02 Rate limiting | B2 | S-08, S-09 | Bonus audit entry added only if implemented |

## Phase details

### P0 — Discovery and decisions

- Branch: none; read-only.
- Goal: replace assumptions with repository and environment facts.
- Inspect: files, `AGENTS.md`, Git status/history/remotes, Java, build tool,
  existing Spring configuration, workflows, Docker availability and test support.
- Produce: architecture proposal, endpoint proposal, authorization proposal,
  test strategy, CI strategy, refined increments and decision recommendations.
- Do not edit files, commit or push.
- Exit gate: student approves P1 scope and D-01/D-02; unresolved later decisions
  remain visibly OPEN.

### P1 — Project bootstrap

- Suggested branch: `chore/project-bootstrap`.
- Suggested increments:
  - P1.1 minimal generated/build structure and wrapper;
  - P1.2 application-context smoke test;
  - P1.3 baseline operational README and documentation update.
- Local gate: clean build, tests pass, application starts under documented local
  assumptions.
- Exit gate: reviewed small commits merged; no secrets or machine-specific paths.

### P2 — Gitea CI foundation

- Suggested branch: `ci/gitea-actions`.
- Goal: run the wrapper-based verification command on pushes and pull requests.
- Confirm an available runner and supported Java environment before writing YAML.
- Keep CI independent of a manually installed developer MongoDB.
- Exit gate: workflow passes on the feature branch, PR and `main`; the equivalent
  local command is documented.

### P3 — MongoDB persistence foundation

- Suggested branch: `feature/persistence-foundation`.
- Suggested increments:
  - P3.1 environment-based MongoDB configuration;
  - P3.2 User and Product documents with meaningful `@Document`, `@Id`, `@Field`;
  - P3.3 repositories, uniqueness behavior and reproducible repository tests.
- Do not expose documents through HTTP yet.
- Exit gate: persistence tests pass locally and in CI; no credentials are committed.

### P4 — HTTP contracts, DTOs and errors

- Suggested branch: `feature/api-contracts`.
- Goal: define request/response DTOs, validation, mapping and a consistent error
  response before public CRUD controllers grow.
- Decide endpoint paths, update semantics and status codes through D-04.
- Exit gate: malformed and invalid input has tested 4XX behavior; response models
  cannot serialize password fields.

### P5 — Safe user creation

- Suggested branch: `feature/secure-registration`.
- Goal: normalize email, reject duplicates, prevent client-selected privileged
  roles and encode passwords with `PasswordEncoder` before persistence.
- Do not create an endpoint that can store a plaintext password, even temporarily.
- Exit gate: tests prove stored passwords are encoded and responses/logs never
  contain plaintext or encoded passwords.

### P6 — Token authentication

- Suggested branch: `feature/token-authentication`.
- Decide D-05/D-06 before implementation.
- Suggested increments: login contract, credential verification, token service,
  authentication filter/provider, safe 401 responses and expiry tests.
- Exit gate: valid tokens authenticate; missing, malformed, invalid and expired
  tokens behave as documented without leaking secrets.

### P7 — Authorization policy

- Suggested branch: `feature/authorization`.
- Goal: implement the approved role/ownership matrix at request and method level.
- `GET /products` (and any approved single-product GET) must be anonymous.
- Registration and login must also be reachable anonymously if the approved design
  exposes them; this is an explicit exception to “authenticated APIs only.”
- Use `@EnableWebSecurity`, `@EnableMethodSecurity`, `@PermitAll`, `@PreAuthorize`
  and `@PostAuthorize` only in meaningful, tested locations.
- Exit gate: 401/403/public-access tests pass and annotation evidence is recorded.

### P8 — User CRUD vertical slice

- Suggested branch: `feature/user-crud`.
- Implement controller -> service -> repository behavior with approved self/admin
  rules, DTOs and error handling.
- Decide deletion consequences through D-08 before delete behavior is added.
- Exit gate: create/read/update/delete success and failure scenarios pass; password
  data is absent from every response.

### P9 — Product CRUD and ownership

- Suggested branch: `feature/product-crud`.
- Implement public reads and protected writes using authenticated ownership rather
  than trusting a client-provided `userId`.
- Exit gate: CRUD, not-found, validation, owner/non-owner/admin and anonymous-read
  scenarios pass.

### P10 — HTTPS and hardening

- Suggested branch: `security/transport-hardening`.
- Provide a local HTTPS profile and certificate-generation instructions without
  committing a private key or secret password.
- Review MongoDB input paths, logs, error payloads, configuration, dependency
  exposure and security headers.
- Exit gate: HTTPS is manually reproducible and documented; mandatory security
  requirements have test or manual evidence.

### P11 — Audit verification and handoff

- Suggested branch: `docs/audit-handoff` unless fixes require feature branches.
- Run the complete automated suite and every manual scenario in
  `docs/AUDIT_PREP.md`.
- Finish README setup, environment, HTTPS, endpoint and Postman/curl instructions.
- Exit gate: every mandatory audit item is `VERIFIED`, CI passes on `main`, and no
  unverified claim remains.

### B1/B2 — Bonus work

Begin only after P11. Treat CORS and rate limiting as separate branches, threat
models, tests and PRs. Bonus work must not destabilize mandatory behavior.

## Per-increment connection checklist

Before editing, write a short scope brief:

```text
Increment:
Requirement IDs:
Study modules:
Audit items:
Files expected to change:
Tests required:
Explicit non-goals:
```

After implementation, the matching learning-log entry must use the same IDs.
The audit-prep table then points to the real code and test evidence.

## Initial prompt for Codex in VS Code

```text
Read AGENTS.md and PLAN.md, then follow their linked documents. Start only with
P0.1, the read-only repository inspection. Do not edit files. Report repository
facts, unresolved decisions and the smallest proposed P1 increment, then wait for
my approval.
```
