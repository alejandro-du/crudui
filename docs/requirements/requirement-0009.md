# requirement-0009

## Summary

Verify hierarchical list support to `crudui2`. The current implementation already ships `GridList` which has a `grid()` method to set a custom `Grid` instance. Vaadin's `TreeGrid` component is a `Grid` so in theory, the application developer only needs to create a custom `TreeGrid` and pass it to `GridList::grid()` and we shouldn't need to make any changes to the API at all. Check this by creating a test view.

## Example

```java
var myTreeGrid = new TreeGrid<Technology>(); // Vaadin's TreeGrid
myTreeGrid.addHierarchyColumn(Technology::getName).setHeader("Name");
myTreeGrid.addColumn(Technology::getVersion).setHeader("Version");
myTreeGrid.addColumn(Technology::getLastPatchedAt).setHeader("Last Patched");

Crud<Technology> technologyCrud = Crud.of(Technology.class)
        .list(GridList.of(Technology.class)
                .grid(myTreeGrid)
                .dataProvider(DataProvider.ofCollection(technologyService.findAll())))
        ... any other required configs ...
```

## Behavior

- A `TreeGrid` can be used as custom `Grid` component.
- The UI shows a tree as a list component.

## Acceptance Criteria

- [ ] No changes to the API are made.
- [ ] CRUD operations work.
- [ ] API behaves as described.
- [ ] Only a new test view is created and other files are untouched. See the instructions in the [test-view-template.md](/docs/requirements/test-view-template.md) file.
- [ ] The new test view allows us to decide whether API changes are needed or not (do not implement this changes just yet).

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
