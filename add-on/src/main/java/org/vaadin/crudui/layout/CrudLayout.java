package org.vaadin.crudui.layout;

import java.util.stream.Stream;

import com.vaadin.flow.component.Component;

import org.vaadin.crudui.crud.CrudOperation;

/**
 * @author Alejandro Duarte
 */
public interface CrudLayout {

	void setMainComponent(Component component);

	void addFilterComponent(Component component);

	default void addFilterComponents(Component... components) {
		Stream.of(components).forEach(this::addFilterComponent);
	}

	void addToolbarComponent(Component component);

	void showForm(CrudOperation operation, Component form, String caption);

	void hideForm();

}
