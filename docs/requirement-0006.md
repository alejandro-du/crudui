# requirement-0006

## Summary

Expose explicit CRUD operations visibility.

## Example

```java
Crud<User> userCrud = Crud.of(User.class)
        .createOperationVisible(false)
        .updateOperationVisible(false)
        .deleteOperationVisible(false)
        .build();
```

## Behavior

- Read, create, update, and delete visibility can be set independently.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
