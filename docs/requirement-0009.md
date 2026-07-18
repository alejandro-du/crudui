# requirement-0009

## Summary

Add hierarchical list support to `crudui2`. The current implementation only ships `GridList`, so tree/hierarchical use cases are not yet supported.

## Example

```java
TreeGrid<Technology> treeGrid = new TreeGrid<>(Technology.class, false);
CrudList<Technology> treeList = new TreeGridList<>(treeGrid);

Crud<Technology> technologyCrud = Crud.of(Technology.class)
        .list(treeList)
        .build();
```

## Behavior

- The `CrudList` contract must support implementations for both flat and hierarchical data.
- The API must provide a `crudui2`-native tree-list implementation (or equivalent adapter) for `TreeGrid`.
- The implementation must support hierarchical data providers for scalable backends.
- Parent-child resolution must be configurable on the list implementation in a way consistent with existing `CrudList` usage.
- Selection, refresh, and form workflows must behave consistently across flat and hierarchical lists.

## Acceptance Criteria

- [ ] API behaves as described.
- [ ] A hierarchy-enabled CRUD works end-to-end in the demo using real services.
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/test-view-template.md) file.

## Remember

Remember from the AGENTS.md file:

- Use the Playwright MCP server to check that the changes are correct!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
