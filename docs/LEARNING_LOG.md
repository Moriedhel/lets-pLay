# Let's Play — Learning Log

This is an append-only chronological record of work actually completed. Planned
work belongs in `../PLAN.md`; general theory belongs in `STUDY_GUIDE.md`.

## Rules

- Add one entry per coherent implementation increment, not per conversational turn.
- Use the same phase, requirement, study and audit IDs as the other documents.
- Record commands and results truthfully; do not invent successful output.
- Explain decisions in relation to the actual diff.
- Update the entry in the same commit as the implementation when practical.
- Do not try to record a commit's own hash inside that commit; Git already does so.
- Do not make a follow-up commit solely to record the preceding CI result. Record
  CI in the PR summary or next meaningful entry.
- Never paste secrets, tokens, passwords or sensitive logs.

## Entry template

Copy this section for each completed increment.

```markdown
## YYYY-MM-DD — P?.? — Short title

- Branch:
- Proposed/approved commit message:
- Requirement IDs:
- Study modules:
- Audit items:
- Status: IN PROGRESS | LOCALLY VERIFIED | CI VERIFIED | MERGED

### Goal and non-goals

What small outcome was targeted? What was deliberately left for later?

### Before implementation

- Repository state inspected:
- Concepts studied:
- Student understanding questions and answers:
- Design approved:

### Files changed

| File | Change | Why |
|---|---|---|

### Implementation and execution flow

Explain what the code does and trace the relevant call flow.

### Design reasoning

- Why this approach:
- Alternatives considered:
- Trade-offs:
- Go comparison, if genuinely useful:

### Java, Spring and annotations

Describe only concepts and annotations introduced or materially practised here.

### Verification

| Command/test | Expected | Actual result |
|---|---|---|

Include positive and negative cases. Link or name relevant test classes.

### Security review

Input trust boundaries, sensitive data, authentication/authorization impact,
query safety, logging and configuration concerns.

### Problems and diagnosis

What failed, how it was investigated and why the final correction works.

### Audit explanation practice

- Question:
- Student's explanation:
- Correction or missing detail:

### Remaining work

List only work intentionally deferred to named future phases.
```

## Entries

## 2026-07-14 — P1.1 — Generate and verify minimal Spring Boot project

- Branch: (committed to main before branching strategy is introduced in P2)
- Proposed/approved commit message: `feat(bootstrap): generate Spring Boot 4.1.0 Maven project scaffold`
- Requirement IDs: REQ-O01
- Study modules: S-01, S-03
- Audit items: AUD-01 (partial — builds and passes smoke test; run instructions deferred to README)
- Status: LOCALLY VERIFIED

### Goal and non-goals

Generate the minimum project skeleton that compiles, passes the context-load smoke
test, and can be started by an auditor. No controllers, persistence, or security yet.

### Before implementation

- Repository state inspected: workspace contained only `AGENTS.md`, `PLAN.md`, `docs/`, `scripts/`.
- Decisions approved: D-01 (Java 21, Spring Boot 4.1.0, Maven wrapper), D-02 (`com.letsplay`, `lets-play`).
- Design approved: single `spring-boot-starter-webmvc` dependency only.

### Files changed

| File | Change | Why |
|---|---|---|
| `pom.xml` | Created | Maven project descriptor; Spring Boot 4.1.0 parent, Java 21, `spring-boot-starter-webmvc` |
| `mvnw` / `mvnw.cmd` | Created | Maven wrapper scripts; no global Maven install required |
| `.mvn/wrapper/` | Created | Wrapper bootstrap; downloads Maven on first run |
| `src/main/java/com/letsplay/LetsPlayApplication.java` | Created | `@SpringBootApplication` entry point |
| `src/test/java/com/letsplay/LetsPlayApplicationTests.java` | Created | Context-load smoke test |
| `src/main/resources/application.properties` | Created | Empty; configuration added per phase |
| `.gitignore` | Created | Ignores `target/`, IDE files, wrapper JAR |

### Implementation and execution flow

1. `.\mvnw.cmd spring-boot:run` triggers the Maven wrapper, which resolves and
   downloads Maven, then invokes the `spring-boot:run` goal.
2. Maven resolves `spring-boot-starter-webmvc` which transitively brings in
   Spring MVC, an embedded Tomcat server, and Jackson for JSON serialization.
3. Spring Boot's `@SpringBootApplication` triggers component scanning and
   auto-configuration. With no controllers or beans present, it starts an
   embedded Tomcat and waits for requests.
4. `.\mvnw.cmd verify` compiles, runs unit and integration tests, and packages.
   `LetsPlayApplicationTests` loads the full Spring context; if it starts without
   throwing, the test passes.

### Java, Spring and annotations

- `@SpringBootApplication`: meta-annotation combining `@Configuration`,
  `@EnableAutoConfiguration`, and `@ComponentScan`. Auto-configuration detects
  `spring-webmvc` on the classpath and registers an embedded Tomcat.
- `pom.xml` `<parent>`: inherits dependency versions and plugin configuration from
  `spring-boot-starter-parent`, so child POMs declare dependencies without versions.
- Maven wrapper (`mvnw.cmd`): the `.mvn/wrapper/maven-wrapper.properties` file pins
  the Maven version used, making the build reproducible without a global install.

### Verification

| Command/test | Expected | Actual result |
|---|---|---|
| `.\mvnw.cmd verify --no-transfer-progress` | BUILD SUCCESS, Tests run: 1, Failures: 0 | BUILD SUCCESS, Tests run: 1, Failures: 0, Errors: 0, Skipped: 0 |

### Security review

No controllers, endpoints, user input, persistence, or credentials exist yet.
The only risk at this stage is committing secrets — none were added.

### Remaining work

- P2: Add `.gitea/workflows/ci.yml` so Gitea CI runs `mvnw verify` on every push.
- P3: Add MongoDB and Flapdoodle (embedded test database).
- README: Add auditor run instructions once the app can receive a meaningful request.

