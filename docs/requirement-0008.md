# requirement-0008

## Summary

Support hierarchical CRUD lists so the API can work with tree-structured data instead of only flat grids.

## Example

```java
Crud<Category> categoryCrud = Crud.of(Category.class)
        .list(new TreeGridList<>(treeGrid))
        .build();
```

## Behavior

- The API supports a tree-capable list implementation for parent-child data.
- The API lets developers define how child items are resolved from a parent item.
- The API accepts hierarchical data providers when the backing data source is not flat.
- Refresh and selection behavior continues to work in hierarchical mode.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
