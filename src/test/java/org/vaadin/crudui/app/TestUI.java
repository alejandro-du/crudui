package org.vaadin.crudui.app;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import org.apache.bval.util.StringUtils;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.EditableGridCrud;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Alejandro Duarte
 */
@Route("")
public class TestUI extends VerticalLayout implements CrudListener<User> {

    private Tabs tabSheet = new Tabs();
    private Div container = new Div();

    public TestUI() {
        JPAService.init();

        container.setSizeFull();

        add(tabSheet, container);
        setSizeFull();

        addCrud(getDefaultCrud(), "Default");
        addCrud(getDefaultCrudWithFixes(), "Default (with fixes)");
        addCrud(getConfiguredCrud(), "Configured");
        addCrud(getEditableGridCrud(), "Editable Grid");
    }

    private void addCrud(Component crud, String caption) {
        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        Tab tab = new Tab(caption);
        tabSheet.add(tab);
        container.add(crud);
        crud.setVisible(false);
        tabSheet.addSelectedChangeListener(e -> {
                crud.setVisible(tabSheet.getSelectedTab() == tab);
        });
    }

    private Component getDefaultCrud() {
        return new GridCrud<>(User.class, this);
    }

    private Component getDefaultCrudWithFixes() {
        GridCrud<User> crud = new GridCrud<>(User.class);
        crud.setCrudListener(this);
        crud.getCrudFormFactory().setFieldProvider("mainGroup", new ComboBoxProvider<>(GroupRepository.findAll()));

        return crud;
    }

    private Component getConfiguredCrud() {
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

        // FIXME no way to set only Grid column order??
        // crud.getGrid().setColumns("name", "birthDate", "email",
        // "phoneNumber",
        // "mainGroup", "active");
        crud.getGrid().removeColumnByKey("mainGroup");
        crud.getGrid().removeColumnByKey("birthDate");
        crud.getGrid().addColumn(new TextRenderer<User>(user -> user == null ? "" : user.getMainGroup().getName()))
                .setHeader("Main group");
        crud.getGrid()
                .addColumn(new LocalDateRenderer<User>(
                        user -> user.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        DateTimeFormatter.ISO_LOCAL_DATE))
                .setHeader("Birthdate");

        formFactory.setFieldType("password", PasswordField.class);
        formFactory.setFieldCreationListener("birthDate", field -> ((DatePicker) field).setLocale(Locale.US));

        formFactory.setFieldProvider("mainGroup",
                new ComboBoxProvider<>("Main Group", GroupRepository.findAll(), Group::getName));

        formFactory.setButtonCaption(CrudOperation.ADD, "Add new user");
        crud.setRowCountCaption("%d user(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);

        return crud;
    }

    private Component getEditableGridCrud() {
        EditableGridCrud<User> crud = new EditableGridCrud<>(User.class, this);

        // FIXME no setColumns in Grid
        // crud.getGrid().setColumns("name", "birthDate", "email",
        // "phoneNumber",
        // "password", "groups", "mainGroup",
        // "active");
        crud.getCrudFormFactory().setVisibleProperties("name", "birthDate", "email", "phoneNumber", "password",
                "groups", "mainGroup", "active");

        crud.getGrid().removeColumnByKey("password");
        crud.getGrid().removeColumnByKey("mainGroup");
        crud.getGrid().removeColumnByKey("groups");

        crud.getGrid().addColumn(new TextRenderer<User>(user -> "********")).setHeader("Password");
        crud.getGrid().addColumn(new TextRenderer<User>(user -> user == null ? "" : user.getMainGroup().getName()));
        crud.getGrid().addColumn(new TextRenderer<User>(user -> StringUtils
                .join((user.getGroups()).stream().map(g -> g.getName()).collect(Collectors.toList()), ", ")));

        crud.getCrudFormFactory().setFieldType("password", PasswordField.class);
        crud.getCrudFormFactory().setFieldProvider("mainGroup",
                new ComboBoxProvider<>(null, GroupRepository.findAll(), group -> group.getName()));

        crud.getCrudFormFactory().setUseBeanValidation(true);

        return crud;
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
