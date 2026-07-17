# requirement-0007

## Summary

Support the older auto-generated form customization model so developers can control property visibility, field types, converters, captions, and validation in a fine-grained way.

## Example

```java
Crud<User> userCrud = Crud.of(User.class)
        .formFactory(factory -> factory
                .setVisibleProperties("name", "email")
                .setDisabledProperties("id")
                .setFieldCaptions("Name", "Email")
                .setUseBeanValidation(true))
        .build();
```

## Behavior

- The form factory lets developers choose which bean properties are visible in the form.
- The form factory lets developers mark individual properties as disabled.
- The form factory lets developers override captions for fields and form actions.
- The form factory lets developers override field types and field providers for specific properties.
- The form factory lets developers attach converters for fields that need value translation.
- The form factory lets developers toggle bean validation support.
- The form factory supports callbacks for field creation and form-level error handling.
- The form factory supports supplying a custom constructor or new-instance supplier for newly created beans.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
