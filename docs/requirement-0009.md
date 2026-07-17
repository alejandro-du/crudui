# requirement-0009

## Summary

Provide layout variants beyond the default split view, including modal window editing and operation-aware form presentation.

## Example

```java
Crud<User> userCrud = Crud.of(User.class)
        .layout(new WindowCrudLayout<>())
        .build();
```

## Behavior

- The API supports a modal or window-based CRUD layout for editing forms.
- The API supports split-based layouts in both horizontal and vertical orientations.
- The layout contract can host a main list component, toolbar components, and filter components.
- The layout can show a form with a caption that depends on the CRUD operation being performed.
- The window-based layout supports a configurable window width and standard dialog interactions.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
