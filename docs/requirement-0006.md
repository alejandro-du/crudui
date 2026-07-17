# requirement-0006

## Summary

Expose explicit CRUD operation controls and backend wiring hooks so developers can enable, disable, and connect read/create/update/delete actions individually when needed.

## Example

```java
Crud<User> userCrud = Crud.of(User.class)
        .setCrudListener(userCrudListener)
        .setAddOperationVisible(true)
        .setUpdateOperationVisible(true)
        .setDeleteOperationVisible(true)
        .setFindAllOperationVisible(true)
        .build();
```

## Behavior

- The API lets developers configure each CRUD operation separately instead of forcing a single all-or-nothing setup.
- Read, create, update, and delete actions can be wired independently.
- The API keeps a convenience path for a single listener, but the lower-level operation hooks must remain available.
- The API exposes a way to supply read data through a `DataProvider` when the caller wants lazy or prebuilt data access.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
