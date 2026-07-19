# requirement-0013

## Summary

Implement converter support in auto-generated forms. The `Field.converter(...)` API already exists but it is not currently applied when binding fields.

## Example

```java
Crud<User> userCrud = Crud.of(User.class)
        .form(CrudFormFactory.of(User.class)
                .defaultFields(
                        Field.<User, BigDecimal, TextField>of(User::getSalary, User::setSalary)
                                .caption("Salary")
                                .converter(new StringToBigDecimalConverter("Invalid salary"))))
        .build();
```

## Behavior

- A converter configured through `Field.converter(...)` must be applied to the underlying binder binding.
- Converter support must work for both field definitions based on method references and field definitions based on property names.
- Converter failures must mark the form invalid and prevent save when `isValid()` is checked.
- Existing field customization APIs (`caption`/`label`, `enabled`, `fieldType`, `fieldProvider`, `onUpdate`, `onValueChangeUpdate`) must continue working.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] A converter configured in `Field` changes model/presentation conversion at runtime.
- [ ] Validation state reflects converter errors.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/requirements/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Wait ~4 seconds after making changes or compiling before checking with Playwright!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
