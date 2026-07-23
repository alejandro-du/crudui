# requirement-0011

## Summary

Add missing fluent hooks in `Crud` for filter registration and operation feedback so applications do not need manual glue code for common scenarios.

## Example

```java
var nameFilter = new TextField();
nameFilter.setPlaceholder("Search by name...");

Crud<User> userCrud = Crud.of(User.class)
        .onCreate(userService::save)
        .onRead(userService::findAll)
        .onUpdate(userService::save)
        .onDelete(userService::delete)
        .addFilterComponent(nameFilter)
        .onSaveSuccess(user -> Notification.show("Saved: " + user.getName()))
        .onDeleteSuccess(user -> Notification.show("Deleted: " + user.getName()))
        .onOperationError(error -> Notification.show(error.getMessage()))
        .build();
```

## Behavior

- `Crud` must expose a fluent `addFilterComponent(Component)` API that delegates to the active layout.
- `Crud` must expose optional success/error callbacks for operation outcomes.
- Existing button customization and delete confirmation customization must keep working.
- New hooks must be optional and preserve current defaults.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] A typical PlaygroundView1 flow can be expressed with less manual glue code.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/requirements/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Wait ~4 seconds after making changes or compiling before checking with Playwright!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
