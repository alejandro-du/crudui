package org.vaadin.crudui.app;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.form.impl.field.provider.RadioButtonGroupProvider;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
@Route("")
public class TestUI extends VerticalLayout implements CrudListener<User> { // or implement LazyCrudListener<User>

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
    private TextField nameFilter = new TextField();
    private ComboBox<Group> groupFilter = new ComboBox<>();

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
        crud.getGrid().setColumns("name", "birthDate", "maritalStatus", "email", "phoneNumber", "active");
        return crud;
    }

    private Component getConfiguredCrud() {
        GridCrud<User> crud = new GridCrud<>(User.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        DefaultCrudFormFactory<User> formFactory = new DefaultCrudFormFactory<>(User.class);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        formFactory.setErrorListener(e -> {
            Notification.show("Custom error message");
            e.printStackTrace();
        });

        formFactory.setVisibleProperties("name", "birthDate", "email", "phoneNumber",
                "maritalStatus", "groups", "active", "mainGroup");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "name", "email", "mainGroup");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("name", "email", "phoneNumber", "active");
        crud.getGrid().addColumn(new LocalDateRenderer<>(
                user -> user.getBirthDate(),
                DateTimeFormatter.ISO_LOCAL_DATE))
                .setHeader("Birthdate");

        crud.getGrid().addColumn(new TextRenderer<>(user -> user == null ? "" : user.getMainGroup().getName()))
                .setHeader("Main group");

        crud.getGrid().setColumnReorderingAllowed(true);

        formFactory.setFieldType("password", PasswordField.class);
        formFactory.setFieldProvider("birthDate", () -> {
            DatePicker datePicker = new DatePicker();
            datePicker.setMax(LocalDate.now());
            return datePicker;
        });

        formFactory.setFieldProvider("maritalStatus", new RadioButtonGroupProvider<>(Arrays.asList(MaritalStatus.values())));
        formFactory.setFieldProvider("groups", new CheckBoxGroupProvider<>("Groups", GroupRepository.findAll(), Group::getName));
        formFactory.setFieldProvider("mainGroup",
                new ComboBoxProvider<>("Main Group", GroupRepository.findAll(), new TextRenderer<>(Group::getName), Group::getName));
        formFactory.setFieldCreationListener(CrudOperation.ADD, "name", f -> f.setValue("default name"));

        formFactory.setButtonCaption(CrudOperation.ADD, "Add new user");
        crud.setRowCountCaption("%d user(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);

        nameFilter.setPlaceholder("filter by name...");
        nameFilter.addValueChangeListener(e -> crud.refreshGrid());
        crud.getCrudLayout().addFilterComponent(nameFilter);

        groupFilter.setPlaceholder("Group");
        groupFilter.setItems(GroupRepository.findAll());
        groupFilter.setItemLabelGenerator(Group::getName);
        groupFilter.addValueChangeListener(e -> crud.refreshGrid());
        crud.getCrudLayout().addFilterComponent(groupFilter);

        Button clearFilters = new Button(null, VaadinIcon.ERASER.create());
        clearFilters.addClickListener(event -> {
            nameFilter.clear();
            groupFilter.clear();
        });
        crud.getCrudLayout().addFilterComponent(clearFilters);

        crud.setFindAllOperation(
                DataProvider.fromCallbacks(
                        query -> UserRepository.findByNameLike(nameFilter.getValue(), groupFilter.getValue(), query.getOffset(), query.getLimit()).stream(),
                        query -> UserRepository.countByNameLike(nameFilter.getValue(), groupFilter.getValue()))
        );
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

    /* if this class implements LazyCrudListener<User>:
    @Override
    public DataProvider<User, Void> getDataProvider() {
        return DataProvider.fromCallbacks(
                query -> UserRepository.findByNameLike(nameFilter.getValue(), groupFilter.getValue(), query.getOffset(), query.getLimit()).stream(),
                query -> UserRepository.countByNameLike(nameFilter.getValue(), groupFilter.getValue()));
    }*/

}
