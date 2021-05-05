package org.vaadin.crudui.demo.ui.view;

import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.CrudOperationException;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.demo.entity.Group;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui.demo.ui.MainLayout;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;

@Route(value = "simple", layout = MainLayout.class)
public class SimpleCrudView extends VerticalLayout {

    public SimpleCrudView(UserService userService, GroupService groupService) {
        // crud instance
        GridCrud<User> crud = new GridCrud<>(User.class);

        // grid configuration
        crud.getGrid().setColumns("id", "name", "birthDate", "maritalStatus", "email", "phoneNumber", "active");
        crud.getGrid().setColumnReorderingAllowed(true);

        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(
                "name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus", "groups", "active", "mainGroup");
        crud.getCrudFormFactory().setVisibleProperties(
                CrudOperation.ADD,
                "name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus", "groups", "active", "mainGroup",
                "password");
        crud.getCrudFormFactory().setFieldProvider("mainGroup",
                new ComboBoxProvider<>(groupService.findAll()));
        crud.getCrudFormFactory().setFieldProvider("groups",
                new CheckBoxGroupProvider<>(groupService.findAll()));
        crud.getCrudFormFactory().setFieldProvider("groups",
                new CheckBoxGroupProvider<>("Groups", groupService.findAll(), Group::getName));
        crud.getCrudFormFactory().setFieldProvider("mainGroup",
                new ComboBoxProvider<>("Main Group", groupService.findAll(), new TextRenderer<>(Group::getName), Group::getName));

        // layout configuration
        setSizeFull();
        add(crud);
        crud.setFindAllOperationVisible(false);

        // logic configuration
        crud.setOperations(
                () -> userService.findAll(),
                user -> userService.save(user),
                user -> {
                    if(user.getId().equals(10L)) {
                	throw new CrudOperationException("Simulated error.");
                    }
		    return userService.save(user);
		},
                user -> userService.delete(user)
        );
    }

}
