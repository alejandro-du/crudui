# requirement-0007

## Summary

Add the missing fluent `form()` and `layout()` entry points in `Crud`.

## Example

```java
Crud<User> userCrud = Crud.of(User.class)
        .form(CrudFormFactory.of(User.class))
        .layout(new SplitCrudLayout<>(Orientation.HORIZONTAL))
        ... other required calls here ...
```

## Behavior

- `Crud` must expose `form(...)` instead of `setCrudFormFactory(...)`.
- `Crud` must expose `layout(...)` instead of `setCrudLayout(...)`.
- `form(...)` and `layout(...)` must return `Crud<B>` and compose naturally with existing chaining.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] `form(...)` and `layout(...)` are available as fluent methods.
- [ ] Old set* methods are no longer used.
- [ ] Layout and form work properly.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/requirements/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
