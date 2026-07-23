# requirement-0001

## Summary

Improve layout and UX:

- There must be spacing or margin (evaluate and decide which one) on the left side of the form
- Action components should be aligned to the right
- After clicking the delete button, a delete confirmation should be shown before actually deleting the item
- Delete confirmation texts should have sensible general defaults but be configurable
- All CRUD buttons texts, icons, variants (primary, tertiary, etc.) must be customizable
- Default CRUD buttons should have icons (use Vaadin!) and empty texts:
  - Create: plus icon
  - Update: pen icon
  - Delete: Trash icon
- Save button should have text and icon (check mark).
- Except when the form is in read-only state, there must be an H3 title to the left of the action components (in the same horizontal line)
  - For create: use title "Add"
  - For update: use title "Update"
- Form titles or captions (decide what term to use) should be customizable per CRUD operation
- When a new item is being created after clicking the create button, the update and delete buttons should be disabled and the grid should not have any item selected
- When the cancel button is clicked, the grid should not have any item selected

## Acceptance Criteria

- [ ] Form has left side margin/spacing
- [ ] Action components are aligned to the right
- [ ] Delete confirmation dialog is shown before deletion
- [ ] Delete confirmation texts are configurable with sensible defaults
- [ ] All CRUD button texts, icons, and variants are customizable
- [ ] CRUD buttons have correct icons and no text
- [ ] Save button has text + icon
- [ ] H3 title ("Add" or "Update") is displayed to the left of action components in non-read-only mode
- [ ] Form titles/captions are customizable per CRUD operation
- [ ] Only files in `add-on/src/main/java/org/vaadin/crudui2/` are touched in the `add-on` Maven module.
- [ ] A new test view exists and showcases the implemented feature. See the instructions in the [test-view-template.md](/docs/requirements/test-view-template.md) file.
- [ ] Update and delete buttons are disabled as specified in the summary.
- [ ] No grid items are selected if the form is not currently showing such item (editable or read-only)

## Remember

- Use the Playwright MCP server to check that the changes are correct!!!
- Use the Vaadin MCP server to check Vaadin APIs!!!
- The demo app is already running and available at http://localhost:8080!!!
