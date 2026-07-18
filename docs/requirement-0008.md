# requirement-0008

## Summary

Provide an operation-aware fluent form factory API so developers can define default fields and per-operation field differences (READ/ADD/UPDATE/DELETE) with minimal boilerplate.

## Example

```java
Crud<User> userCrud = Crud.of(User.class)
        .form(CrudFormFactory.of(User.class)
                .fields(
                        Field.of(User::getName, User::setName).caption("The name"),
                        Field.of(User::getEmail, User::setEmail).caption("The email"),
                        Field.of(User::getGroups, User::setGroups).caption("The groups"))
                .fieldsFor(CrudOperation.DELETE, // sets the fields for this operation
                        Field.of(User::getName, User::setName).caption("The name"),
                        Field.of(User::getEmail, User::setEmail).caption("The email"))
                .additionalFieldsFor(CrudOperation.ADD, // add as oppose to set (add to what is already there)
                        Field.of(User::getPassword, User::setPassword).caption("The password")))
        .build();
```

## Behavior

- `CrudFormFactory.of(Class<B>)` must provide a fluent entry point for form configuration.
- The API must support defining a default set of fields used by operations unless overridden.
- The API must support replacing field sets per operation (for example `fieldsFor(DELETE, ...)`).
- The API must support adding extra fields per operation without redefining the full set (for example `additionalFieldsFor(ADD, ...)`).
- Method-reference and property-based field definitions must both be supported.
- Existing field customization capabilities (`fieldProvider`, `fieldType`, update hooks, validation support) must remain available.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] Developers can express common CRUD form variants (default, delete subset, add extra fields) with the fluent API.
- [ ] The resulting form configuration is operation-aware without requiring manual conditional form building code.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
