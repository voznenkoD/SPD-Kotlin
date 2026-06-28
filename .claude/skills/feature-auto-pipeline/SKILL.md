---
description: Autonomous feature pipeline. Use when the user wants to go from a
  high-level feature idea to reviewed, implemented code changes in one flow,
  with the ONLY human gate being the product-owner clarification/confirmation.
  After requirements are confirmed the pipeline runs to completion on its own
  (build → review → bounded fix loop) and stops at file changes (no commit).
---

# Feature Auto Pipeline

Deliver the feature end-to-end for: $ARGUMENTS

This is the autonomous sibling of `feature-pipeline`. There is exactly ONE
interactive gate — the requirements clarification in Phase 1. Once the user
confirms requirements, every later phase runs automatically with NO further
prompts, questions, or approvals, until the work is delivered as code changes.

## Phase 1 — Requirements (the ONLY interactive step)
Invoke /product-owner with the feature description above.
- Ask the user any clarifying questions needed to remove ambiguity.
- Present the structured requirements and get explicit confirmation.
- If the user requests changes, update and re-confirm.
This is the last point at which you ask the user anything. After confirmation,
announce that the pipeline is going autonomous and proceed without stopping.

## Phase 2 — Implementation (autonomous)
Invoke /build-feature with the confirmed requirements.
Ensure the project compiles (e.g. `./gradlew compileKotlinDesktop`). If it does
not, fix the compilation as part of this phase — do not ask the user.

## Phase 3 — Review (autonomous)
Invoke /code-review (workflow-backed) on the working changes.
Collect findings grouped by severity. Treat as blocking only:
- **Critical design issues** (architecture/correctness flaws, broken behavior)
- **Code bugs** (crashes, wrong results, regressions)
Lower-severity cleanups/style/perf-polish are NOT blocking and are NOT a reason
to keep looping.

## Phase 4 — Bounded autonomous fix loop (max 2 iterations)
If Phase 3 surfaced blocking findings (critical design issues or code bugs):
1. Pass them to /build-feature to fix.
2. Re-run /code-review.
Repeat at most **2 fix iterations total**. Apply fixes automatically — never ask
the user which to take. You MAY also fold in high-confidence, low-risk cleanups
while you're there, but the loop's purpose is blocking issues only.

Exit the loop when EITHER:
- a review pass has no remaining critical design issues or code bugs, OR
- 2 fix iterations have been completed.

If blocking findings still remain after 2 iterations, STOP looping and report
them clearly in Phase 5 as known residual issues (do not keep going, do not ask).

Keep the project compiling at the end of every iteration.

## Phase 5 — Done (no commit)
Report:
- what was built (summary),
- files changed/added,
- the requirements file and test plan paths,
- review result: blocking issues fixed, iterations used, and any residual
  critical/bug findings left after the 2-iteration cap.

Leave all changes in the working tree. Do NOT commit, branch, or push. You may
mention the user can review and commit themselves (e.g. with /commit), but do
not run it.

## Rules
- The ONLY time you prompt the user is the Phase 1 clarification/confirmation.
  After requirements are confirmed, run fully autonomously — no confirmations,
  no "should I proceed?", no per-warning approvals.
- Fix loop is capped at 2 iterations and targets critical design issues and
  code bugs; do not loop on style/polish.
- Never commit, branch, or push — the deliverable is the file changes only.
- Always keep the build compiling.
- If a phase hard-fails in a way you cannot resolve autonomously (e.g.
  unrecoverable build break, missing tool), STOP and report the failure clearly
  rather than guessing.
