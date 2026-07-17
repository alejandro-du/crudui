package org.vaadin.crudui.demo.ui.view.test;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui2.Crud;

@Route(value = "requirement-0006")
public class Requirement0006 extends VerticalLayout {

    public Requirement0006(UserService userService, GroupService groupService) {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        Crud<User> noCreateUpdateDeleteCrud = Crud.of(User.class)
            .onRead(userService::findAll)
            .onCreate(userService::save)
            .onUpdate(userService::save)
            .onDelete(userService::delete)
            .createOperationVisible(false)
            .updateOperationVisible(false)
            .deleteOperationVisible(false)
            .build();

        Crud<User> createOnlyCrud = Crud.of(User.class)
            .onRead(userService::findAll)
            .onCreate(userService::save)
            .onUpdate(userService::save)
            .onDelete(userService::delete)
            .createOperationVisible(true)
            .updateOperationVisible(false)
            .deleteOperationVisible(false)
            .build();

        add(
            new H3("Create/update/delete hidden"),
            noCreateUpdateDeleteCrud,
            new H3("Create visible, update/delete hidden"),
            createOnlyCrud
        );
    }
}
