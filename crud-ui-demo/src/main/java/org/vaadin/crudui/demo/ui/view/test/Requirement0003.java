package org.vaadin.crudui.demo.ui.view.test;

import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui2.Crud;
import org.vaadin.crudui2.list.impl.GridList;

/**
 * Test view for requirement-0003: demonstrate fluent CRUD setup with explicit
 * DataProvider.
 *
 * This view demonstrates two approaches using Vaadin Tabs:
 * 1. Read wiring via DataProvider
 * 2. Read wiring via supplier method reference
 */
@Route(value = "requirement-0003")
public class Requirement0003 extends VerticalLayout {

    public Requirement0003(UserService userService) {
        setPadding(false);
        setSpacing(false);
        setSizeFull();

        // Create tabs
        Tabs tabs = new Tabs();

        // Tab 1: Lambda expressions approach
        VerticalLayout lambdaPanel = new VerticalLayout();
        lambdaPanel.setPadding(true);
        lambdaPanel.setSpacing(true);
        lambdaPanel.setSizeFull();
        Crud<User> crudWithLambda = Crud.of(User.class)
                .list(GridList.of(User.class))
                .onCreate(userService::save)
                .onRead(DataProvider.ofCollection(userService.findAll()))
                .onUpdate(userService::save)
                .onDelete(userService::delete)
                .build();
        lambdaPanel.add(crudWithLambda);

        Tab lambdaTab = new Tab("onRead(DataProvider)");
        tabs.add(lambdaTab);

        // Tab 2: Custom list builder approach
        VerticalLayout listenerPanel = new VerticalLayout();
        listenerPanel.setPadding(true);
        listenerPanel.setSpacing(true);
        listenerPanel.setSizeFull();
        Crud<User> crudWithListener = Crud.of(User.class)
                .list(GridList.of(User.class))
                .onRead(userService::findAll)
                .onCreate(userService::save)
                .onUpdate(userService::save)
                .onDelete(userService::delete)
                .build();
        listenerPanel.add(crudWithListener);

        Tab listenerTab = new Tab("onRead(userService::findAll)");
        tabs.add(listenerTab);

        // Create a container for the tab content
        VerticalLayout contentContainer = new VerticalLayout();
        contentContainer.setPadding(false);
        contentContainer.setSpacing(false);
        contentContainer.setSizeFull();
        contentContainer.add(lambdaPanel);

        // Handle tab selection
        tabs.addSelectedChangeListener(event -> {
            contentContainer.removeAll();
            if (event.getSelectedTab() == lambdaTab) {
                contentContainer.add(lambdaPanel);
            } else if (event.getSelectedTab() == listenerTab) {
                contentContainer.add(listenerPanel);
            }
        });

        // Add tabs and content to main layout
        add(tabs);
        add(contentContainer);
        setFlexGrow(1, contentContainer);
    }
}
