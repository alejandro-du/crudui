# requirement-0003

## Summary

Support both `CrudListener` and lambda expressions for integrating backend services. Refactor the `CrudListener` interface to have `onCreate`, `onRead`, `onUpdate`, and `onDelete` methods. Make sure both work by using the demo app `UserService`.

## Example

```java
Crud crud1 = Crud.of(User.class)
        .onCreate(userService::save)
        .onRead(userService::findAll)
        .onUpdate(userService::save)
        .onDelete(userService::delete)
        .build();

Crud crud2 = Crud.of(User.class)
        .setCrudListener(this) // the containing class should implement the interface methods
        .build();
```

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- The demo app is already running and available at http://localhost:8080!!!
