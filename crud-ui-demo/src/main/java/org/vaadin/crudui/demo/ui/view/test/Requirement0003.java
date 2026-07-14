package org.vaadin.crudui.demo.ui.view.test;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui2.Crud;
import org.vaadin.crudui2.CrudListener;

/**
 * Test view for requirement-0003: Support both CrudListener and lambda expressions.
 *
 * This view demonstrates two approaches using Vaadin Tabs:
 * 1. Lambda expressions approach using onRead, onCreate, onUpdate, onDelete
 * 2. CrudListener interface approach using setCrudListener
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
            .onRead(userService::findAll)
            .onCreate(userService::save)
            .onUpdate(userService::save)
            .onDelete(userService::delete)
            .build();
        lambdaPanel.add(crudWithLambda);

        Tab lambdaTab = new Tab("Lambda Expressions");
        tabs.add(lambdaTab);

        // Tab 2: CrudListener interface approach
        VerticalLayout listenerPanel = new VerticalLayout();
        listenerPanel.setPadding(true);
        listenerPanel.setSpacing(true);
        listenerPanel.setSizeFull();
        Crud<User> crudWithListener = Crud.of(User.class)
            .setCrudListener(new UserCrudListener(userService))
            .build();
        listenerPanel.add(crudWithListener);

        Tab listenerTab = new Tab("CrudListener Interface");
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

    /**
     * Example CrudListener implementation.
     */
    private static class UserCrudListener implements CrudListener<User> {
        private final UserService userService;

        public UserCrudListener(UserService userService) {
            this.userService = userService;
        }

        @Override
        public java.util.List<User> onRead() {
            return userService.findAll();
        }

        @Override
        public void onCreate(User user) {
            userService.save(user);
        }

        @Override
        public void onUpdate(User user) {
            userService.save(user);
        }

        @Override
        public void onDelete(User user) {
            userService.delete(user);
        }
    }
}
