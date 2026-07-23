# requirement-0012

## Summary

Allow custom new-entity creation in `Crud` so create mode does not require a public no-args constructor.

## Example

```java
Crud<User> userCrud = Crud.of(User.class)
        .form()
                .newInstanceSupplier(() -> {
                    var user = new User(); // it's okay to have it here for simplicity but it shouldn't be needed
                    user.setSalary(... some default salary value...);
                    return user;
                })
        ... other configurations ...
        .build();
```

## Behavior

- `Crud` must allow callers to provide a supplier used when the Create action is clicked.
- If a supplier is provided, `Crud` must use it instead of reflective instantiation.
- If no supplier is provided, current default behavior must stay unchanged.
- Supplier-created instances must still work with form generation, validation, and save flow.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] Create mode works for entities without a public no-args constructor when a supplier is configured.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/requirements/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Wait ~4 seconds after making changes or compiling before checking with Playwright!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
