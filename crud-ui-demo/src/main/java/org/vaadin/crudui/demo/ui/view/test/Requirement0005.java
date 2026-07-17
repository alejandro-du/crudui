package org.vaadin.crudui.demo.ui.view.test;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui2.Crud;
import org.vaadin.crudui2.CrudAction;

@Route(value = "requirement-0005")
public class Requirement0005 extends VerticalLayout {

    public Requirement0005(UserService userService, GroupService groupService) {
        setSizeFull();
        setPadding(true);

        Crud<User> userCrud = Crud.of(User.class)
            .onRead(userService::findAll)
            .onCreate(userService::save)
            .onUpdate(userService::save)
            .onDelete(userService::delete)
            .configureButton(CrudAction.CREATE, "Add User", VaadinIcon.PLUS.create(), ButtonVariant.LUMO_PRIMARY)
            .configureButton(CrudAction.UPDATE, "Edit", VaadinIcon.EDIT.create(), ButtonVariant.LUMO_PRIMARY)
            .configureButton(CrudAction.DELETE, "Remove", VaadinIcon.TRASH.create(), ButtonVariant.LUMO_ERROR)
            .configureButton(CrudAction.SAVE, "Persist", VaadinIcon.CHECK.create(), ButtonVariant.LUMO_PRIMARY)
            .configureButton(CrudAction.CANCEL, "Back", VaadinIcon.CLOSE_SMALL.create(), ButtonVariant.LUMO_TERTIARY)
            .formCaptions("Create User", "Update User")
            .deleteConfirmationTexts(
                "Delete selected user?",
                "This will permanently remove the selected user.",
                "Delete user",
                "Keep user")
            .viewBeforeEdit(true)
            .build();

        add(userCrud);
    }
}
