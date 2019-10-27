package org.vaadin.crudui.demo.ui.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
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

@Route(value = "simple", layout = MainLayout.class)
public class SimpleCrudView extends VerticalLayout {

    public SimpleCrudView(UserService userService, GroupService groupService) {
        // crud columns and form fields
        GridCrud<User> crud = new GridCrud<>(User.class);
        //GridCrud<User> crud3 = new GridCrud<>(User.class, new SpringDataListener(userRepository));

        // grid configuration
        crud.getGrid().setColumns("name", "birthDate", "maritalStatus", "email", "phoneNumber", "active");

        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setProperties("name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus",
                "groups", "active", "mainGroup");
        crud.getCrudFormFactory().addProperty(CrudOperation.ADD, "password");

        // form fields configuration
        crud.getCrudFormFactory().getProperties("groups").stream().forEach(
                property -> property.setFieldProvider(
                        new CheckBoxGroupProvider<>("Groups", groupService.findAll(), Group::getName))
        );

        crud.getCrudFormFactory().getProperties("mainGroup").stream().forEach(
                property -> property.setFieldProvider(
                        new ComboBoxProvider<>("Main Group", groupService.findAll(), new TextRenderer<>(Group::getName),
                                Group::getName))
        );

        crud.getCrudFormFactory().getProperty(CrudOperation.ADD, "password").setFieldType(PasswordField.class);

        // layout configuration
        setSizeFull();
        add(crud);

        // logic configuration
        crud.setOperations(
                () -> userService.findAll(),
                user -> userService.save(user),
                user -> userService.save(user),
                user -> userService.delete(user)
        );
    }

}
