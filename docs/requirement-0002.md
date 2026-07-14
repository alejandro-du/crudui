# requirement-0002

## Summary

Accept custom `Grid` components.

## Example

```java
var myGrid = new Grid<User>();
myGrid.addColumn(User::getId);
myGrid.addColumn(User::getName).setHeader("The name of the user");
myGrid.addColumn(User::getBirthDate).setHeader("The birth date of the user");

Crud crud = Crud.of(User.class)
		.list(new GridList<>(myGrid))
        .build();
```

## Behavior

- If a list is specified, the library creates a CRUD component that uses the specified list instead of using the default.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- The demo app is already running and available at http://localhost:8080!!!
