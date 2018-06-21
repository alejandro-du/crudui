package org.vaadin.crudui.app;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;

/**
 * @author Alejandro Duarte.
 */
@Route("crud")
public class TestUI2 extends VerticalLayout implements CrudListener<User> {

    public TestUI2() {
        TextField textField = new TextField();
        textField.setPlaceholder("filter by name");

        GridCrud<User> crud = new GridCrud<>(User.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        GridLayoutCrudFormFactory<User> formFactory = new GridLayoutCrudFormFactory<>(User.class, 2, 2);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)"));

        formFactory.setVisibleProperties(CrudOperation.READ, "id", "name", "birthDate", "email", "phoneNumber",
                "groups", "active", "mainGroup");
        formFactory.setVisibleProperties(CrudOperation.ADD, "name", "birthDate", "email", "phoneNumber", "groups",
                "password", "mainGroup", "active");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "id", "name", "birthDate", "email", "phoneNumber",
                "password", "groups", "active", "mainGroup");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "name", "email", "phoneNumber");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("name", "birthDate", "email", "phoneNumber", "mainGroup", "active");
        crud.getGrid().removeColumnByKey("mainGroup");
        crud.getGrid().removeColumnByKey("birthDate");
        crud.getGrid().addColumn(new TextRenderer<>(user -> user == null ? "" : user.getMainGroup().getName()))
                .setHeader("Main group");
        crud.getGrid()
                .addColumn(new LocalDateRenderer<>(
                        user -> user.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        DateTimeFormatter.ISO_LOCAL_DATE))
                .setHeader("Birthdate");

        formFactory.setFieldType("password", PasswordField.class);
        formFactory.setFieldCreationListener("birthDate", field -> ((DatePicker) field).setLocale(Locale.US));

        formFactory.setFieldProvider("groups", new CheckBoxGroupProvider<>("Groups", GroupRepository.findAll(), Group::getName));
        formFactory.setFieldProvider("mainGroup",
                new ComboBoxProvider<>("Main Group", GroupRepository.findAll(), Group::getName));

        formFactory.setButtonCaption(CrudOperation.ADD, "Add new user");
        crud.setRowCountCaption("%d user(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);
        crud.getCrudLayout().addFilterComponent(textField);

        add(crud);
        setSizeFull();
        setMargin(false);
        setPadding(false);
    }

    @Override
    public User add(User user) {
        UserRepository.save(user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId().equals(5l)) {
            throw new RuntimeException("A simulated error has occurred");
        }
        return UserRepository.save(user);
    }

    @Override
    public void delete(User user) {
        UserRepository.delete(user);
    }

    @Override
    public Collection<User> findAll() {
        return UserRepository.findAll();
    }

}
