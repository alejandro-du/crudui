package org.vaadin.crudui.demo.ui.view.test;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui2.Crud;
import org.vaadin.crudui2.list.impl.GridList;

/**
 * Test view showcasing custom Grid components with Crud.list() API.
 *
 * Demonstrates:
 * - Creating a custom Grid with specific columns
 * - Wrapping the Grid in a GridList
 * - Passing the custom list to Crud via the fluent .list() method
 * - Custom Grid configuration and styling is preserved
 */
@Route(value = "requirement-0002")
public class Requirement0002 extends VerticalLayout {

    public Requirement0002(UserService userService, GroupService groupService) {
        setSizeFull();
        setPadding(true);

        // Create a custom Grid with specific columns
        var myGrid = new Grid<User>();
        myGrid.addColumn(User::getId).setHeader("ID");
        myGrid.addColumn(User::getName).setHeader("Name");
        myGrid.addColumn(User::getEmail).setHeader("Email");
        myGrid.addColumn(User::getBirthDate).setHeader("Birth Date");

        // Create CRUD with custom grid
        Crud<User> userCrud = Crud.of(User.class)
                .list(GridList.of(User.class).grid(myGrid)
                        .dataProvider(DataProvider.ofCollection(userService.findAll())))
                .onCreate(userService::save)
                .onUpdate(userService::save)
                .onDelete(userService::delete)
                .build();

        // Add the CRUD component to layout
        add(userCrud);
    }
}
