# requirement-0004

## Summary

Allow read-only forms and edit button. By default, the CRUD component shows an editable form when an item is clicked (for example in a grid). A new option is needed to allow users to see a read-only form when items are clicked.

## Example

```java
Crud<User> userCrud = Crud.of(User.class)
        .viewBeforeEdit(true); // false is the default
        .build();
```

## Behavior

- When `viewBeforeEdit` is enabled, an "Update" action component is shown in the layout component to enable the form for editing.
- The "Update" action component is disabled if there are no items selected in the list.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/requirements/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- The demo app is already running and available at http://localhost:8080!!!
