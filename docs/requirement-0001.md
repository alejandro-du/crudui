# requirement-0001

## Summary

Create a simple CRUD component with sensible defaults.

## Example

```java
Crud myCrud = Crud.of(User.class);
```

## Behavior

- The library creates a default CRUD component ready to be added to a Vaadin layout.
- By default, it must include a `GridList` in a horizontal `SplitCrudLayout`.
- The list and form must include all the properties available in the specified domain class (Java bean).
- A "Create" action component should be added to the CRUD layout via the `addFormActionComponent` method.
- When the user clicks a row in the list, a form is added to the layout via the existing `showCrudForm` method.
- A "Save" action component should be added to the form via the existing `addFormActionComponent` method, when the form gets visible.
- When the user deselects a row (no rows selected), the form is hidden.

See the PlaygroundView1.java file for inspiration on how to implement all this. Do NOT modify PlaygroundView1.java!

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/test-view-template.md) file.
