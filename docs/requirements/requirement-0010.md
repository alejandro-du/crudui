# requirement-0010

## Summary

Add an additional implementation of `CrudLayout` that supports dialog (popup window) forms. In theory, no other changes should be required, but please evaluate.

## Example

```java
Crud<User> userCrud = Crud.of(User.class)
        .layout(DialogCrudLayout.of(User.class)
        .build();
```

## Behavior

- This layout shows a `CrudList` component with the area for crud action components on top aligned to the left.
- If no items are selected, only the Create action component is enabled, the others are disabled.
- When items are clicked, Update and Delete action components are enabled as well.
- When Update is clicked, a Dialog is opened to show the form for editing.
- When Delete is clicked, a confirmation is shown.
- The new layout must implement the corresponding existing interface in a similar way `SplitCrudLayout` does.
- Dialog-style layouts must support standard sizing and usability configuration.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] The same CRUD operations and form behavior work unchanged after layout swap.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/requirements/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
