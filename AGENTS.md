# Crud UI agent instructions

This project contains the `add-on` library and the `crud-ui-demo` application.
Read `docs/vision.md` before changing the add-on API. Check the current Maven
files and relevant requirements rather than assuming versions or behavior.

## Product changes

- Preserve the type-safe, fluent API and the contracts described in the vision.
  Do not change the values of `CrudOperation`.
- Unless explicitly instructed otherwise, modify product code only under
  `add-on/src/main/java/org/vaadin/crudui/` and
  `crud-ui-demo/src/main/java/org/vaadin/crudui/demo/ui/view/test/`. Follow
  `docs/requirements/test-view-template.md` for requirement test views.
- Do not edit generated output under `target/`. Use existing Vaadin APIs when
  suitable; consult available Vaadin documentation when needed.
- Do not start or stop the demo application. If a running demo and browser
  tooling are available, verify relevant UI changes there; otherwise report
  the limitation.

## Development and verification

- Inspect relevant code and tests before changing behavior. Make focused
  changes, preserve existing behavior unless requested otherwise, and update
  directly affected documentation.
- Add focused automated regression tests for new or changed behavior where
  practical. A requirement test view does not replace automated tests.
- Run the smallest appropriate checks and report what passed, what failed, and
  what could not be verified. From the repository root, use explicit Maven
  goals:
  - `mvn -pl add-on test` runs the add-on tests.
  - `mvn -pl crud-ui-demo -am test` tests the add-on and builds the demo in the
    same reactor, so the demo uses the current add-on. The demo currently has
    no automated tests.

## Working safely

- Inspect existing changes before editing and preserve unrelated work. Do not
  use Git to modify files, create commits, switch branches, or integrate work;
  the human controls Git changes.
- Ask for approval before consequential actions. Instructions and agent roles
  are guidance, not access controls; inspect tool permissions and commands.
- Keep changes focused. Capture a lesson only when it is durable, verified and
  useful, with evidence; propose a concise guidance change for human review
  rather than appending automatic logs.
