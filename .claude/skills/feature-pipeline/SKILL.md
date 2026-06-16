---
description: Full feature pipeline. Use when the user wants to go from a feature
  idea all the way to reviewed, implemented code in one flow.
---

# Feature Pipeline

Run the full feature delivery chain for: $ARGUMENTS

## Phase 1 — Requirements
Invoke /product-owner with the feature description above.
Present the generated requirements to the user and wait for confirmation
before proceeding. If the user requests changes, update requirements and
confirm again.

## Phase 2 — Implementation
Once requirements are confirmed, invoke /build-feature with the
confirmed requirements as input.

## Phase 3 — Review
Once build-feature completes, automatically invoke /code-reviewer.
Present findings grouped by severity.

## Phase 4 — Fix Loop (if needed)
If /code-reviewer finds Critical or Warning issues, pass them back to
/build-feature to fix. Then run /code-reviewer again.
Only proceed when review passes with no Critical findings.

## Phase 5 — Done
Report completion: what was built, files changed, review result.
Ask user if they want to commit with /commit.

## Rules
- Never skip the user confirmation between Phase 1 and Phase 2
- Never auto-fix Warning issues without asking — present them and let
  the user decide
- If any phase fails, stop and report the failure clearly