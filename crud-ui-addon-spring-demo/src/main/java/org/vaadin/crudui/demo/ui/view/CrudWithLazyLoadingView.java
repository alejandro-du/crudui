package org.vaadin.crudui.demo.ui.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.LazyCrudListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.demo.entity.Group;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui.demo.ui.MainLayout;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.spring.OffsetBasedPageRequest;

@Route(value = "lazy-loading", layout = MainLayout.class)
public class CrudWithLazyLoadingView extends VerticalLayout {

    public CrudWithLazyLoadingView(UserService userService, GroupService groupService) {
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

        // grid configuration
        crud.getGrid().setPageSize(50);

        // layout configuration
        setSizeFull();
        add(crud);

        // logic configuration
        crud.setCrudListener(new LazyCrudListener<User>() {
            @Override
            public DataProvider<User, Void> getDataProvider() {
                return DataProvider.fromCallbacks(
                        query -> userService.findAll(new OffsetBasedPageRequest(query)).stream(),
                        query -> userService.countAll());
            }

            @Override
            public User add(User user) {
                return userService.save(user);
            }

            @Override
            public User update(User user) {
                return userService.save(user);
            }

            @Override
            public void delete(User user) {
                userService.delete(user);
            }
        });
    }

}
