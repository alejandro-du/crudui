package org.vaadin.crudui.demo.ui.view;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.form.impl.field.provider.MultiSelectComboBoxProvider;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;
import org.vaadin.crudui.layout.impl.WindowBasedCrudLayout;

@Route(value = "customized", layout = MainLayout.class)
public class CustomizedView extends VerticalLayout implements LazyCrudListener<User> {

	private UserService userService;
	private HasValue<?, String> nameFilter;

	public CustomizedView(UserService userService, GroupService groupService) {
		this.userService = userService;

		// CRUD layout configuration
		WindowBasedCrudLayout crudLayout = new WindowBasedCrudLayout();
		crudLayout.setFormWindowWidth("70%");

		// CRUD forms configuration
		CrudFormFactory<User> formFactory = new DefaultCrudFormFactory<>(User.class);
		formFactory.setUseBeanValidation(true);
		formFactory.setCaption(CrudOperation.ADD, "Create new User");

		formFactory.setVisibleProperties(
				"name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus", "groups", "active",
				"mainGroup");
		formFactory.setFieldCaptions(
				"The name", "The birthdate", "The e-mail", "The Salary", "The phone number", "The marital status",
				"The groups", "Is it active?", "The main group", "The password");
		formFactory.setVisibleProperties(CrudOperation.ADD,
				"name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus", "groups", "active", "mainGroup",
				"password");

		formFactory.setVisibleProperties(CrudOperation.DELETE, "name", "mainGroup");

		formFactory.setFieldProvider("mainGroup", new ComboBoxProvider<>(groupService.findAll()));
		formFactory.setFieldProvider("groups",
				new MultiSelectComboBoxProvider<>("Groups", groupService.findAll(), Group::getName));
		formFactory.setFieldProvider("mainGroup", new ComboBoxProvider<>("Main Group", groupService.findAll(),
				new TextRenderer<>(Group::getName), Group::getName));

		// CRUD component configuration
		GridCrud<User> crud = new GridCrud<>(User.class, crudLayout, formFactory, this);
		crud.setClickRowToUpdate(true);
		crud.setUpdateOperationVisible(false);
		crud.setDeleteOperationVisible(false);

		// grid configuration
		Grid<User> grid = crud.getGrid();
		grid.setColumns("name", "birthDate", "maritalStatus", "email", "phoneNumber");
		grid.getColumnByKey("name").setAutoWidth(true);
		grid.getColumnByKey("email").setAutoWidth(true);
		grid.setColumnReorderingAllowed(true);
		grid.addComponentColumn(user -> {
			var button = new Button(VaadinIcon.TRASH.create(), event -> crud.showDeleteConfirmation(user));
			button.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
			return button;
		}).setFlexGrow(0);

		// filters
		nameFilter = crud.addFilterProperty("Filter by name");

		// view configuration
		setSizeFull();
		add(crud);
	}

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

}
