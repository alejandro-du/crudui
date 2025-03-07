package org.vaadin.crudui.demo.ui.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.demo.entity.Group;
import org.vaadin.crudui.demo.entity.MaritalStatus;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui2.form.CrudForm;
import org.vaadin.crudui2.form.CrudFormFactory;
import org.vaadin.crudui2.form.field.CrudField;
import org.vaadin.crudui2.form.field.provider.ComboBoxProvider;
import org.vaadin.crudui2.form.field.provider.MultiSelectComboBoxProvider;

@Route(value = "playground")
@JsModule("theme-handler.js")
public class PlaygroundView extends VerticalLayout {

	public PlaygroundView(UserService userService, GroupService groupService) {
		var mainGroupField = CrudField.of(User::getMainGroup, User::setMainGroup, Group.class);
		var mainGroupField2 = CrudField.of("mainGroup");

		CrudForm<User> form = new CrudFormFactory<>(User.class)
				//*
				.setFields(
						CrudField.of(User::getName, User::setName, String.class).label("Name").enabled(false),
						CrudField.of(User::getMaritalStatus, User::setMaritalStatus, MaritalStatus.class).label("Marital status"),
						CrudField.of(User::getBirthDate, User::setBirthDate, LocalDate.class).label("Birth date"),
						CrudField.of(User::getSalary, User::setSalary, BigDecimal.class).label("Salary"),
						CrudField.of(User::getGroups, User::setGroups, Set.class).label("In groups").fieldProvider(new MultiSelectComboBoxProvider<Group>(groupService.findAll(), Group::getName)).onValueChangeUpdate(mainGroupField),
						mainGroupField.label("Main group").fieldProvider(new ComboBoxProvider<>(Arrays.asList(new Group()), Group::getName)).onUpdate(this::populateMainGroupBox),
						CrudField.of(User::getActive, User::setActive, Boolean.class).label("Active"))
				//*/
				//*
				.add(CrudField.of(User::getName, User::setName, String.class).label("Name").enabled(false))
				.add(CrudField.of(User::getMaritalStatus, User::setMaritalStatus, MaritalStatus.class).label("Marital status"))
				.add(CrudField.of(User::getBirthDate, User::setBirthDate, LocalDate.class).label("Birth date"))
				.add(CrudField.of(User::getSalary, User::setSalary, BigDecimal.class).label("Salary"))
				.add(CrudField.of(User::getGroups, User::setGroups, Set.class).label("In groups").fieldProvider(new MultiSelectComboBoxProvider<Group>(groupService.findAll(), Group::getName)).onValueChangeUpdate(mainGroupField))
				.add(mainGroupField.label("Main group").fieldProvider(new ComboBoxProvider<>(Arrays.asList(new Group()), Group::getName)).onUpdate(this::populateMainGroupBox))
				.add(CrudField.of(User::getActive, User::setActive, Boolean.class).label("Active"))
				//*/
				//**
				.useBeanValidation()
				.setFields(
						CrudField.of("name").label("The name"),
						CrudField.of("birthDate").label("The date of birth"),
						CrudField.of("email").label("The email"),
						CrudField.of("salary").label("The salary"),
						CrudField.of("phoneNumber").label("The phone number"),
						CrudField.of("maritalStatus").label("The marital status"),
						CrudField.of("groups").label("The groups").fieldProvider(new MultiSelectComboBoxProvider<Group>(groupService.findAll(), Group::getName)).onValueChangeUpdate(mainGroupField2),
						mainGroupField2.label("The main group").fieldProvider(new ComboBoxProvider<>(Arrays.asList(new Group()), Group::getName)).onUpdate(this::populateMainGroupBox),
						CrudField.of("active").label("Is it active?"))
				//*/
				.build();

		User user = userService.findByNameContainingIgnoreCase("Edgar", 1, 2).get().findFirst().orElse(null);
		form.setValue(user);

		var save = new Button("Save", e -> {
			if (form.isValid()) {
				try {
					userService.save(user);
					Notification.show("Saved: " + user);

				} catch (Exception ex) {
					Notification.show("Error: " + ex.getMessage());
					return;
				}
			} else {
				Notification.show("Form is not valid");
			}
		});

		add(form, save);
		setSizeFull();
		UI.getCurrent().getPage().executeJs("window.applySystemTheme()");
	}

	private void populateMainGroupBox(AbstractField<?, ?> field, User user) {
		ComboBox<Group> comboBox = (ComboBox<Group>) field;
		Group previousMainGroup = user.getMainGroup();
		comboBox.setItems(user.getGroups());
		if(user.getGroups().contains(previousMainGroup)) {
			comboBox.setValue(previousMainGroup);
		}
	}

	private void populateMainGroupBox(AbstractField<?, ?> field, Object bean) {
		ComboBox<Group> comboBox = (ComboBox<Group>) field;
		User user = (User) bean;
		Group previousMainGroup = user.getMainGroup();
		comboBox.setItems(user.getGroups());
		if(user.getGroups().contains(previousMainGroup)) {
			comboBox.setValue(previousMainGroup);
		}
	}
}
