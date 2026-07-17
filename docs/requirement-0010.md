# requirement-0010

## Summary

Restore the older CRUD convenience behaviors around grid interaction, filtering, feedback messages, and button accessors.

## Example

```java
Crud<User> userCrud = Crud.of(User.class)
        .setClickRowToUpdate(true)
        .setRowCountCaption("%d items found")
        .setSavedMessage("Saved")
        .setDeletedMessage("Deleted")
        .build();
```

## Behavior

- The API can add a filter field directly from a label or caption.
- The API can update the form when a row is clicked, instead of only opening read-only details.
- The API exposes the main CRUD buttons so applications can inspect or customize them after construction.
- The API can show row-count feedback after refresh.
- The API can show save and delete feedback messages after successful operations.
- The API can disable or hide notification behavior when applications want silent operation.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
