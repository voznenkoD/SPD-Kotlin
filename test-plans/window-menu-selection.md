# Test Plan: Move screen selection from "View" menu to "Window" menu

Feature spec: `features/window-menu-selection.md`
Code under test: `composeApp/src/desktopMain/kotlin/org/xebia/spdmanager/App.kt` (MenuBar block)

## Scope
Menu-only UI change. No model/service/viewmodel logic affected. Manual verification
via the running desktop app is the primary form of testing (Compose `MenuBar`
items are not unit-testable in this project's setup).

## Manual Test Cases

### TC1 — Menu bar structure
1. Run `./gradlew desktopRun`.
2. Inspect the menu bar.
- [ ] Menus appear in order: **File**, **View**, **Window**.
- [ ] **View** menu opens and is empty (no items).
- [ ] **Window** menu opens and shows three items: **Main**, **Setup**, **System**.

### TC2 — Mnemonics
- [ ] **View** menu responds to its `V` mnemonic.
- [ ] **Window** menu responds to its `W` mnemonic.

### TC3 — Radio selection reflects current screen
1. On launch (default screen = Main), open the **Window** menu.
- [ ] **Main** shows the radio/selected indicator; Setup and System do not.
2. Click **Setup**, then re-open **Window**.
- [ ] **Setup** is now selected; Main and System are not.
3. Click **System**, then re-open **Window**.
- [ ] **System** is now selected; Main and Setup are not.

### TC4 — Navigation behavior (no regression)
1. Select **Window > Main** → MainScreen renders.
2. Select **Window > Setup** → SetupScreen renders.
3. Select **Window > System** → SystemScreen renders.
- [ ] Each selection switches the displayed screen correctly.
- [ ] Switching screens does not reset or lose any loaded device data.

## Regression Checks
- [ ] `./gradlew compileKotlinDesktop` succeeds.
- [ ] File menu (Choose folder / Save) still works as before.