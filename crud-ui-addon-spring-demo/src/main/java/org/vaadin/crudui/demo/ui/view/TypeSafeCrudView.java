package org.vaadin.crudui.demo.ui.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.demo.entity.Group;
import org.vaadin.crudui.demo.entity.MaritalStatus;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui.demo.ui.MainLayout;
import org.vaadin.crudui.form.Property;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Route(value = "type-safe", layout = MainLayout.class)
public class TypeSafeCrudView extends VerticalLayout implements CrudListener<User> {

    private final UserService userService;
    private final GroupService groupService;

    public TypeSafeCrudView(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;

        // crud columns and form fields
        GridCrud<User> crud = new GridCrud<>(User.class);

        // grid configuration
        crud.getGrid().removeAllColumns(); // TODO: shouldn't be required
        crud.getGrid().addColumn(User::getName).setHeader("Name");
        crud.getGrid().addColumn(User::getBirthDate).setHeader("Birth Date");
        crud.getGrid().addColumn(User::getMaritalStatus).setHeader("Marital Status");
        crud.getGrid().addColumn(User::getEmail).setHeader("Email");
        crud.getGrid().addColumn(User::getPhoneNumber).setHeader("Phone Number");
        crud.getGrid().addColumn(User::getActive).setHeader("Active");

        // form and form fields configuration
        crud.getCrudFormFactory().addProperty(String.class, User::getName, User::setName).forEach(
                (o, p) -> p.setFieldCaption("Name"));
        crud.getCrudFormFactory().addProperty(LocalDate.class, User::getBirthDate, User::setBirthDate).forEach(
                (o, p) -> p.setFieldCaption("Birth Date"));
        crud.getCrudFormFactory().addProperty(String.class, User::getEmail, User::setEmail).forEach(
                (o, p) -> p.setFieldCaption("Email"));
        crud.getCrudFormFactory().addProperty(BigDecimal.class, User::getSalary, User::setSalary).forEach(
                (o, p) -> p.setFieldCaption("Salary"));
        crud.getCrudFormFactory().addProperty(Integer.class, User::getPhoneNumber, User::setPhoneNumber).forEach(
                (o, p) -> p.setFieldCaption("Phone Number"));
        crud.getCrudFormFactory().addProperty(MaritalStatus.class, User::getMaritalStatus,
                User::setMaritalStatus).forEach((o, p) -> p.setFieldCaption("Marital Status"));
        crud.getCrudFormFactory().addProperty(Set.class, User::getGroups, User::setGroups).forEach(
                (o, p) -> ((Property) p).setFieldProvider(
                        new CheckBoxGroupProvider<>("Groups", groupService.findAll(), Group::getName))
        );
        crud.getCrudFormFactory().addProperty(Boolean.class, User::getActive, User::setActive).forEach(
                (o, p) -> p.setFieldCaption("Active"));
        crud.getCrudFormFactory().addProperty(Group.class, User::getMainGroup, User::setMainGroup).forEach(
                (o, p) -> p.setFieldProvider(
                        new ComboBoxProvider<>("Main Group", groupService.findAll(), new TextRenderer<>(Group::getName),
                                Group::getName))
        );
        crud.getCrudFormFactory().addProperty(CrudOperation.ADD, String.class, User::getPassword,
                User::setPassword).setFieldCaption("Password")
                .setFieldType(PasswordField.class);

        // layout configuration
        setSizeFull();
        add(crud);
    }

    @Override
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @Override
    public User add(User user) {
        // TODO: validation
        return userService.save(user);
    }

    @Override
    public User update(User user) {
        // TODO: validation
        return userService.save(user);
    }

    @Override
    public void delete(User user) {
        userService.delete(user);
    }

}
