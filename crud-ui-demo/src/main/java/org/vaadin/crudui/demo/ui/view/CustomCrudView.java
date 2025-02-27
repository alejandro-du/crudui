package org.vaadin.crudui.demo.ui.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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

@Route(value = "simple", layout = MainLayout.class)
public class CustomCrudView extends VerticalLayout {

	public CustomCrudView(UserService userService, GroupService groupService) {

		// crud instance
		GridCrud<User> crud = new GridCrud<>(User.class);

		// additional components
		TextField filter = new TextField();
		filter.setPlaceholder("Filter by name");
		filter.setClearButtonVisible(true);
		filter.addValueChangeListener(e -> crud.refreshGrid());

		// grid configuration
		crud.getCrudLayout().addFilterComponent(filter);
		crud.getGrid().setColumns("id", "name", "birthDate", "maritalStatus", "email", "phoneNumber", "active");
		crud.getGrid().setColumnReorderingAllowed(true);
		crud.setClickRowToUpdate(true);

		// form configuration
		crud.getCrudFormFactory().setUseBeanValidation(true);
		crud.getCrudFormFactory().setCaption(CrudOperation.ADD, "Create new User");
		crud.getCrudFormFactory().setVisibleProperties(
				"name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus", "groups", "active",
				"mainGroup");
		crud.getCrudFormFactory().setVisibleProperties(
				CrudOperation.ADD,
				"name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus", "groups", "active", "mainGroup",
				"password");
		crud.getCrudFormFactory().setFieldCaptions(
				"The name", "The birthdate", "The e-mail", "The Salary", "The phone number", "The marital status",
				"The groups", "Is it active?",
				"The main group", "The password");
		crud.getCrudFormFactory().setFieldProvider("mainGroup",
				new ComboBoxProvider<>(groupService.findAll()));
		crud.getCrudFormFactory().setFieldProvider("groups",
				new CheckBoxGroupProvider<>(groupService.findAll()));
		crud.getCrudFormFactory().setFieldProvider("groups",
				new CheckBoxGroupProvider<>("Groups", groupService.findAll(), Group::getName));
		crud.getCrudFormFactory().setFieldProvider("mainGroup",
				new ComboBoxProvider<>("Main Group", groupService.findAll(), new TextRenderer<>(Group::getName),
						Group::getName));

		// layout configuration
		setSizeFull();
		add(crud);
		crud.setUpdateOperationVisible(false);

		// logic configuration
		crud.setCrudListener(new LazyCrudListener<>() {
			@Override
			public DataProvider<User, Void> getDataProvider() {
				return DataProvider.fromCallbacks(
						query -> userService.findByNameContainingIgnoreCase(
								filter.getValue(), query.getPage(), query.getPageSize()).stream(),
						query -> (int) userService.countByNameContainingIgnoreCase(filter.getValue()));
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
