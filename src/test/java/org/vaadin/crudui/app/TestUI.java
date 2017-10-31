package org.vaadin.crudui.app;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.TextRenderer;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.crudui.crud.Crud;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.EditableGridCrud;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;
import org.vaadin.jetty.VaadinJettyServer;

import java.sql.Date;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alejandro Duarte
 */
public class TestUI extends UI implements CrudListener<User> {

    public static void main(String[] args) throws Exception {
        JPAService.init();
        VaadinJettyServer server = new VaadinJettyServer(8080, TestUI.class);
        server.start();
    }

    private TabSheet tabSheet = new TabSheet();

    @Override
    protected void init(VaadinRequest request) {
        tabSheet.setSizeFull();
        setContent(tabSheet);

        addCrud(getDefaultCrud(), "Default");
        addCrud(getDefaultCrudWithFixes(), "Default (with fixes)");
        addCrud(getConfiguredCrud(), "Configured");
        addCrud(getEditableGridCrud(), "Editable Grid");
    }

    private void addCrud(Crud crud, String caption) {
        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        tabSheet.addTab(layout, caption);
    }

    private Crud getDefaultCrud() {
        return new GridCrud<>(User.class, this);
    }

    private Crud getDefaultCrudWithFixes() {
        GridCrud<User> crud = new GridCrud<>(User.class);
        crud.setCrudListener(this);
        crud.getCrudFormFactory().setFieldProvider("groups", new CheckBoxGroupProvider<>(GroupRepository.findAll()));
        crud.getCrudFormFactory().setFieldProvider("mainGroup", new ComboBoxProvider<>(GroupRepository.findAll()));

        return crud;
    }

    private Crud getConfiguredCrud() {
        GridCrud<User> crud = new GridCrud<>(User.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        GridLayoutCrudFormFactory<User> formFactory = new GridLayoutCrudFormFactory<>(User.class, 2, 2);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);
        formFactory.setJpaTypeForJpaValidation(JPAService.getFactory().getMetamodel().managedType(User.class));

        formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));

        formFactory.setVisibleProperties(CrudOperation.READ, "id", "name", "birthDate", "email", "phoneNumber", "groups", "active", "mainGroup");
        formFactory.setVisibleProperties(CrudOperation.ADD, "name", "birthDate", "email", "phoneNumber", "groups", "password", "mainGroup", "active");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "id", "name", "birthDate", "email", "phoneNumber", "password", "groups", "active", "mainGroup");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "name", "email", "phoneNumber");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("name", "birthDate", "email", "phoneNumber", "mainGroup", "active");
        crud.getGrid().getColumn("mainGroup").setRenderer(group -> group == null ? "" : ((Group) group).getName(), new TextRenderer());
        ((Grid.Column<User, Date>) crud.getGrid().getColumn("birthDate")).setRenderer(new DateRenderer("%1$tY-%1$tm-%1$te"));

        formFactory.setFieldType("password", PasswordField.class);
        formFactory.setFieldCreationListener("birthDate", field -> ((DateField) field).setDateFormat("yyyy-MM-dd"));

        formFactory.setFieldProvider("groups", new CheckBoxGroupProvider<>("Groups", GroupRepository.findAll(), Group::getName));
        formFactory.setFieldProvider("mainGroup", new ComboBoxProvider<>("Main Group", GroupRepository.findAll(), Group::getName));

        formFactory.setButtonCaption(CrudOperation.ADD, "Add new user");
        crud.setRowCountCaption("%d user(s) found");

        return crud;
    }

    private Crud getEditableGridCrud() {
        EditableGridCrud<User> crud = new EditableGridCrud<>(User.class, this);

        crud.getGrid().setColumns("name", "birthDate", "email", "phoneNumber", "password", "groups", "mainGroup", "active");
        crud.getCrudFormFactory().setVisibleProperties("name", "birthDate", "email", "phoneNumber", "password", "groups", "mainGroup", "active");

        crud.getGrid().getColumn("password").setRenderer(user -> "********", new TextRenderer());
        crud.getGrid().getColumn("mainGroup").setRenderer(group -> group == null ? "" : ((Group) group).getName(), new TextRenderer());
        crud.getGrid().getColumn("groups").setRenderer(groups -> StringUtils.join(((Set<Group>) groups).stream().map(g -> g.getName()).collect(Collectors.toList()), ", "), new TextRenderer());

        crud.getCrudFormFactory().setFieldType("password", PasswordField.class);
        crud.getCrudFormFactory().setFieldProvider("groups", new CheckBoxGroupProvider<>(null, GroupRepository.findAll(), group -> group.getName()));
        crud.getCrudFormFactory().setFieldProvider("mainGroup", new ComboBoxProvider<>(null, GroupRepository.findAll(), group -> group.getName()));

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
