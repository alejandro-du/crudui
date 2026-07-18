# requirement-0010

## Summary

Add at least one additional layout implementation to `crudui2` beyond `SplitCrudLayout`.

## Example

```java
Crud<User> userCrud = Crud.of(User.class)
        .layout(new DialogCrudLayout<>())
        .build();
```

## Behavior

- The API must support at least one additional layout strategy in `crudui2` (for example dialog/modal).
- Layouts must implement the same `CrudLayout` contract so they are swappable.
- When `layout(...)` or `setCrudLayout(...)` is used, the current list must be wired into the new layout automatically.
- CRUD actions, form actions, captions, and filters must keep equivalent behavior when switching layouts.
- Dialog-style layouts must support standard sizing and usability configuration.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] A CRUD can switch layouts by changing one fluent configuration call.
- [ ] The same CRUD operations and form behavior work unchanged after layout swap.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
