package org.vaadin.crudui.demo.ui.view;

import java.util.List;

import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.demo.entity.Group;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui.demo.ui.MainLayout;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;

@Route(value = "split-layout", layout = MainLayout.class)
public class CrudWithSplitLayoutView extends VerticalLayout {

    public CrudWithSplitLayoutView(UserService userService, GroupService groupService) {
      
        // form configuration
	DefaultCrudFormFactory<User> formFactory = new DefaultCrudFormFactory<User>(User.class) {
	    @Override
	    protected void configureForm(FormLayout formLayout, List<HasValueAndElement> fields) {
		Component nameField = (Component) fields.get(0);
	        formLayout.setColspan(nameField, 2);
	    }
	};
        formFactory.setUseBeanValidation(true);
        formFactory.setVisibleProperties(
                "name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus", "groups", "active", "mainGroup");
        formFactory.setVisibleProperties(
            CrudOperation.ADD,
            "name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus", "groups", "active", "mainGroup",
            "password");
        formFactory.setFieldProvider("mainGroup",
                new ComboBoxProvider<>(groupService.findAll()));
        formFactory.setFieldProvider("groups",
                new CheckBoxGroupProvider<>(groupService.findAll()));
        formFactory.setFieldProvider("groups",
                new CheckBoxGroupProvider<>("Groups", groupService.findAll(), Group::getName));
        formFactory.setFieldProvider("mainGroup",
                new ComboBoxProvider<>("Main Group", groupService.findAll(), new TextRenderer<>(Group::getName), Group::getName));
      
        // crud instance
        GridCrud<User> crud = new GridCrud<>(User.class, new HorizontalSplitCrudLayout(), formFactory);
        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);

        // grid configuration
        crud.getGrid().setColumns("name", "birthDate", "maritalStatus", "email", "phoneNumber", "active");
        crud.getGrid().setColumnReorderingAllowed(true);
        
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
