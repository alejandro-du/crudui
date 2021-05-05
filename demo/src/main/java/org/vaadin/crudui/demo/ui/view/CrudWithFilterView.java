package org.vaadin.crudui.demo.ui.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.demo.entity.Group;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui.demo.ui.MainLayout;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;

@Route(value = "filter", layout = MainLayout.class)
public class CrudWithFilterView extends VerticalLayout {

    public CrudWithFilterView(UserService userService, GroupService groupService) {
        // crud instance
        GridCrud<User> crud = new GridCrud<>(User.class);

        // additional components
        TextField filter = new TextField();
        filter.setPlaceholder("Filter by name");
        filter.setClearButtonVisible(true);
        crud.getCrudLayout().addFilterComponent(filter);

        // grid configuration
        crud.getGrid().setColumns("name", "birthDate", "maritalStatus", "email", "phoneNumber", "active");
        crud.getGrid().setColumnReorderingAllowed(true);

        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(
                CrudOperation.ADD,
                "name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus", "groups", "active", "mainGroup",
                "password");
        crud.getCrudFormFactory().setVisibleProperties(
                "name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus", "groups", "active", "mainGroup");
        crud.getCrudFormFactory().setFieldProvider("mainGroup",
                new ComboBoxProvider<>(groupService.findAll()));
        crud.getCrudFormFactory().setFieldProvider("groups",
                new CheckBoxGroupProvider<>(groupService.findAll()));
        crud.getCrudFormFactory().setFieldProvider("groups",
                new CheckBoxGroupProvider<Group>(groupService.findAll(), Group::getName));
        crud.getCrudFormFactory().setFieldProvider("mainGroup",
                new ComboBoxProvider<Group>(groupService.findAll(), new TextRenderer<>(Group::getName), Group::getName));

        // layout configuration
        setSizeFull();
        add(crud);

        // logic configuration
        crud.setOperations(
                () -> userService.findByNameContainingIgnoreCase(filter.getValue()),
                user -> userService.save(user),
                user -> userService.save(user),
                user -> userService.delete(user)
        );

        filter.addValueChangeListener(e -> crud.refreshGrid());
    }

}
