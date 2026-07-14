# Let's Play — Codex Working Agreement

## Purpose

Help build the Let's Play Spring Boot API as an educational project. Work in small,
reviewable increments. The student must understand and be able to defend every
change during an audit.

## Read order

Before proposing or changing code, read:

1. `docs/PROJECT_SPEC.md` — what the exercise and audit require.
2. `PLAN.md` — current phase, dependencies, decisions and exit criteria.
3. Relevant sections of `docs/STUDY_GUIDE.md` — concepts needed now.
4. `docs/LEARNING_LOG.md` — work already completed.
5. `docs/AUDIT_PREP.md` — evidence still needed.

The user's current instruction has highest priority. If the documents conflict,
stop and point out the conflict instead of silently choosing an interpretation.

## Scope control

- Work only on the active phase named in `PLAN.md`.
- Do not implement the whole application at once.
- Do not start bonus work until every mandatory requirement is verified.
- Do not add a dependency, architecture pattern or annotation merely because it
  is popular or appears in an audit checklist. Explain its purpose first.
- Preserve existing and uncommitted work. Inspect the worktree before edits.
- Do not rewrite unrelated files.

## Phase workflow

For every implementation increment, follow this loop:

1. **Discover** — inspect the repository and current phase state.
2. **Connect** — name the requirement IDs, study-module IDs and audit items served.
3. **Study** — explain the just-in-time concepts and ask 2–3 short questions.
4. **Design** — propose the smallest change, files affected and tests required.
5. **Approve** — wait for the student's confirmation before editing.
6. **Implement** — make only the approved coherent change.
7. **Verify** — run the relevant local build, tests and checks.
8. **Document** — update the study guide, learning log and audit evidence accurately.
9. **Review** — show the diff summary and proposed Conventional Commit message.
10. **Commit** — wait for approval, then create the commit.
11. **Push** — wait for approval, push and check Gitea CI.
12. **Close** — fix failures before continuing; open/merge a PR only with approval.

Never claim a check, behavior, CI run or audit item passed unless it was actually
verified.

## Git and Gitea rules

- Keep `main` buildable and tested. It may be incomplete until the final phase.
- Use short-lived branches for coherent features; do not create one branch per
  tiny commit or one branch for the whole project.
- Start branches from an updated `main` with a clean worktree.
- Use small Conventional Commits and push regularly so CI runs.
- Never commit or push directly to `main` unless the user explicitly requests it.
- Do not commit, push, open a PR, merge or delete a branch without approval.
- Do not start a new increment while relevant local checks or CI are failing.
- Never commit credentials, tokens, passwords, private keys, keystores containing
  private keys, production certificates or real connection strings.

## Engineering rules

- Keep persistence documents, request DTOs, response DTOs, authentication DTOs,
  business logic, HTTP logic and security logic separate.
- Never return or log plaintext passwords, password hashes, tokens, private keys
  or sensitive request bodies.
- Use Spring Data repositories instead of building queries from raw client input.
- Validate and normalize input at appropriate boundaries.
- Never trust a client-supplied role during public registration.
- Model expected client failures as suitable 4XX responses. Do not disguise errors
  as successful responses. Log unexpected failures safely.
- Use constructor injection. Because the audit explicitly checks `@Autowired`, it
  may be placed on an appropriate constructor and explained; avoid field injection.
- Use audit-requested annotations only where their behavior is meaningful and
  covered by a test or documented manual verification.
- Add or update tests with each behavior change.

## Documentation ownership

- `README.md`: how an auditor installs, configures, runs and calls the application.
- `docs/PROJECT_SPEC.md`: requirements and unresolved interpretation questions.
- `PLAN.md`: sequencing, branches, dependencies, decision gates and phase status.
- `docs/STUDY_GUIDE.md`: concepts, examples, misconceptions and study status.
- `docs/LEARNING_LOG.md`: chronological record of work actually completed.
- `docs/AUDIT_PREP.md`: traceability from audit question to code, test and evidence.

Avoid duplicating the same long explanation. Link to the owning document instead.

## Definition of done for an increment

An increment is complete only when:

- Its approved scope is implemented.
- Relevant tests pass locally.
- No secret or sensitive response field was introduced.
- Required documentation is updated.
- The diff has been reviewed with the student.
- The approved commit is pushed.
- Gitea CI passes.

Phase-level completion also requires every exit criterion in `PLAN.md`.

