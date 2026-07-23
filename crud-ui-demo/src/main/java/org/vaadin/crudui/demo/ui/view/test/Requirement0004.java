package org.vaadin.crudui.demo.ui.view.test;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.Crud;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui.list.impl.GridList;

/**
 * Test view for requirement-0004: Allow read-only forms and edit button.
 *
 * This view demonstrates the viewBeforeEdit feature using Vaadin Tabs:
 * 1. Standard mode: Form is editable immediately when item is selected
 * 2. View-before-edit mode: Form is read-only initially with an "Update" button
 * to enable editing
 */
@Route(value = "requirement-0004")
public class Requirement0004 extends VerticalLayout {

    public Requirement0004(UserService userService) {
        setPadding(false);
        setSpacing(false);
        setSizeFull();

        // Create tabs
        Tabs tabs = new Tabs();

        // Tab 1: View-before-edit mode (viewBeforeEdit = true)
        VerticalLayout viewBeforeEditPanel = new VerticalLayout();
        viewBeforeEditPanel.setPadding(true);
        viewBeforeEditPanel.setSpacing(true);
        viewBeforeEditPanel.setSizeFull();
        Crud<User> viewBeforeEditCrud = Crud.of(User.class)
                .list(GridList.of(User.class))
                .onCreate(userService::save)
                .onRead(userService::findAll)
                .onUpdate(userService::save)
                .onDelete(userService::delete)
                .viewBeforeEdit(true) // Form is read-only initially with Update button
                .build();
        viewBeforeEditPanel.add(viewBeforeEditCrud);

        Tab viewBeforeEditTab = new Tab("View-Before-Edit Mode");
        tabs.add(viewBeforeEditTab);

        // Tab 2: Standard mode (viewBeforeEdit = false)
        VerticalLayout standardPanel = new VerticalLayout();
        standardPanel.setPadding(true);
        standardPanel.setSpacing(true);
        standardPanel.setSizeFull();
        Crud<User> standardCrud = Crud.of(User.class)
                .list(GridList.of(User.class))
                .onRead(userService::findAll)
                .onCreate(userService::save)
                .onUpdate(userService::save)
                .onDelete(userService::delete)
                .viewBeforeEdit(false) // Form is editable immediately
                .build();
        standardPanel.add(standardCrud);

        Tab standardTab = new Tab("Standard Mode");
        tabs.add(standardTab);

        // Create a container for the tab content
        VerticalLayout contentContainer = new VerticalLayout();
        contentContainer.setPadding(false);
        contentContainer.setSpacing(false);
        contentContainer.setSizeFull();
        contentContainer.add(viewBeforeEditPanel);

        // Handle tab selection
        tabs.addSelectedChangeListener(event -> {
            contentContainer.removeAll();
            if (event.getSelectedTab() == standardTab) {
                contentContainer.add(standardPanel);
            } else if (event.getSelectedTab() == viewBeforeEditTab) {
                contentContainer.add(viewBeforeEditPanel);
            }
        });

        // Add tabs and content to main layout
        add(tabs);
        add(contentContainer);
        setFlexGrow(1, contentContainer);
    }

}
