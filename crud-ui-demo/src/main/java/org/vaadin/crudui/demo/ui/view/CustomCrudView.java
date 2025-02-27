package org.vaadin.crudui.demo.ui.view;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.CrudOperationException;
import org.vaadin.crudui.crud.LazyCrudListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.demo.entity.Group;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui.demo.ui.MainLayout;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;

@Route(value = "simple", layout = MainLayout.class)
public class CustomCrudView extends VerticalLayout {

	public CustomCrudView(UserService userService, GroupService groupService) {

		// crud instance
		GridCrud<User> crud = new GridCrud<>(User.class);
		crud.setClickRowToUpdate(true);
		crud.setUpdateOperationVisible(false);
		HasValue<?, String> nameFilter = crud.addFilterProperty("Filter by name");

		// grid configuration
		Grid<User> grid = crud.getGrid();
		grid.setColumns("id", "name", "birthDate", "maritalStatus", "email", "phoneNumber", "active");
		grid.setColumnReorderingAllowed(true);

		// form configuration
		CrudFormFactory<User> formFactory = crud.getCrudFormFactory();
		formFactory.setUseBeanValidation(true);
		formFactory.setCaption(CrudOperation.ADD, "Create new User");
		formFactory.setVisibleProperties(
				"name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus", "groups", "active",
				"mainGroup");
		formFactory.setVisibleProperties(
				CrudOperation.ADD,
				"name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus", "groups", "active", "mainGroup",
				"password");
		formFactory.setFieldCaptions(
				"The name", "The birthdate", "The e-mail", "The Salary", "The phone number", "The marital status",
				"The groups", "Is it active?", "The main group", "The password");
		formFactory.setFieldProvider("mainGroup", new ComboBoxProvider<>(groupService.findAll()));
		formFactory.setFieldProvider("groups", new CheckBoxGroupProvider<>(groupService.findAll()));
		formFactory.setFieldProvider("groups",
				new CheckBoxGroupProvider<>("Groups", groupService.findAll(), Group::getName));
		formFactory.setFieldProvider("mainGroup", new ComboBoxProvider<>("Main Group", groupService.findAll(),
				new TextRenderer<>(Group::getName), Group::getName));

		// layout configuration
		setSizeFull();
		add(crud);

		// logic configuration
		crud.setCrudListener(new LazyCrudListener<>() {
			@Override
			public DataProvider<User, Void> getDataProvider() {
				return DataProvider.fromCallbacks(
						query -> userService.findByNameContainingIgnoreCase(
								nameFilter.getValue(), query.getPage(), query.getPageSize()).stream(),
						query -> (int) userService.countByNameContainingIgnoreCase(nameFilter.getValue()));
			}

			@Override
			public User add(User user) {
				return userService.save(user);
			}

			@Override
			public User update(User user) {
				if (user.getId().equals(1L)) {
					throw new CrudOperationException("Simulated exception (only for user ID 1).");
				}
				return userService.save(user);
			}

			@Override
			public void delete(User user) {
				userService.delete(user);
			}
		});

	}

}
