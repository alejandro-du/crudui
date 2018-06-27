package org.vaadin.crudui.app;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.LazyCrudListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;

/**
 * @author Alejandro Duarte
 */
@Route("")
public class TestUI extends VerticalLayout implements LazyCrudListener<User> {

    @WebListener
    public static class ContextListener implements ServletContextListener {

        @Override
        public void contextInitialized(ServletContextEvent sce) {
            JPAService.init();
        }

        @Override
        public void contextDestroyed(ServletContextEvent sce) {
            JPAService.close();
        }
    }

    private Tabs tabSheet = new Tabs();
    private VerticalLayout container = new VerticalLayout();
    private TextField filter = new TextField();

    public TestUI() {
        tabSheet.setWidth("100%");

        container.setSizeFull();
        container.setMargin(false);
        container.setPadding(false);

        add(tabSheet, container);
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        addCrud(getDefaultCrud(), "Default");
        addCrud(getMinimal(), "Minimal");
        addCrud(getConfiguredCrud(), "Configured");
    }

    private void addCrud(Component crud, String caption) {
        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        Tab tab = new Tab(caption);
        tabSheet.add(tab);
        container.add(crud);
        crud.setVisible(tabSheet.getChildren().count() == 1);
        tabSheet.addSelectedChangeListener(e -> crud.setVisible(tabSheet.getSelectedTab() == tab));
    }

    private Component getDefaultCrud() {
        return new GridCrud<>(User.class, this);
    }

    private Component getMinimal() {
        GridCrud<User> crud = new GridCrud<>(User.class);
        crud.setCrudListener(this);
        crud.getCrudFormFactory().setFieldProvider("mainGroup", new ComboBoxProvider<>(GroupRepository.findAll()));
        crud.getCrudFormFactory().setFieldProvider("groups", new CheckBoxGroupProvider<>(GroupRepository.findAll()));
        crud.getGrid().setColumns("name", "birthDate", "gender", "email", "phoneNumber", "active");
        return crud;
    }

    private Component getConfiguredCrud() {
        GridCrud<User> crud = new GridCrud<>(User.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        DefaultCrudFormFactory<User> formFactory = new DefaultCrudFormFactory<>(User.class);
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

        crud.getGrid().setColumns("name", "email", "phoneNumber", "active");
        crud.getGrid().addColumn(new LocalDateRenderer<>(
                user -> user.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                DateTimeFormatter.ISO_LOCAL_DATE))
                .setHeader("Birthdate");

        crud.getGrid().addColumn(new TextRenderer<>(user -> user == null ? "" : user.getMainGroup().getName()))
                .setHeader("Main group");

        crud.getGrid().setColumnReorderingAllowed(true);

        formFactory.setFieldType("password", PasswordField.class);
        formFactory.setFieldCreationListener("birthDate", field -> ((DatePicker) field).setLocale(Locale.US));

        formFactory.setFieldProvider("groups", new CheckBoxGroupProvider<>("Groups", GroupRepository.findAll(), Group::getName));
        formFactory.setFieldProvider("mainGroup",
                new ComboBoxProvider<>("Main Group", GroupRepository.findAll(), Group::getName));

        formFactory.setButtonCaption(CrudOperation.ADD, "Add new user");
        crud.setRowCountCaption("%d user(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);


        filter.setPlaceholder("filter by name...");
        crud.getCrudLayout().addFilterComponent(filter);
        filter.addValueChangeListener(e -> crud.refreshGrid());
        /*crud.setFindAllOperation(
                DataProvider.fromCallbacks(
                        query -> UserRepository.findByNameLike(filter.getValue(), query.getOffset(), query.getLimit()).stream(),
                        query -> UserRepository.countByNameLike(filter.getValue()))
        );*/
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

    @Override
    public DataProvider<User, Void> getDataProvider() {
        return DataProvider.fromCallbacks(
                query -> UserRepository.findByNameLike(filter.getValue(), query.getOffset(), query.getLimit()).stream(),
                query -> UserRepository.countByNameLike(filter.getValue()));
    }

}
