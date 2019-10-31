package org.vaadin.crudui.demo.ui.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
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
        // crud columns and form fields
        GridCrud<User> crud = new GridCrud<>(User.class);

        // grid configuration
        crud.getGrid().setColumns("name", "birthDate", "maritalStatus", "email", "phoneNumber", "active");

        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setProperties("name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus",
                "groups", "active", "mainGroup");
        crud.getCrudFormFactory().addProperty(CrudOperation.ADD, "password");

        // form fields configuration
        crud.getCrudFormFactory().getProperty("groups").stream().forEach(
                property -> property.setFieldProvider(
                        new CheckBoxGroupProvider<>("Groups", groupService.findAll(), Group::getName))
        );

        crud.getCrudFormFactory().getProperty("mainGroup").stream().forEach(
                property -> property.setFieldProvider(
                        new ComboBoxProvider<>("Main Group", groupService.findAll(), new TextRenderer<>(Group::getName),
                                Group::getName))
        );

        crud.getCrudFormFactory().getProperty(CrudOperation.ADD, "password").setFieldType(PasswordField.class);

        // additional components
        TextField filter = new TextField();
        filter.setPlaceholder("Filter by name");
        filter.setClearButtonVisible(true);
        crud.getCrudLayout().addFilterComponent(filter);

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
