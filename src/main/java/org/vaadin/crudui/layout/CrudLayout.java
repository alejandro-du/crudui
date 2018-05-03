package org.vaadin.crudui.layout;

import org.vaadin.crudui.crud.CrudOperation;

import com.vaadin.flow.component.Component;

/**
 * @author Alejandro Duarte
 */
public interface CrudLayout {

    void setCaption(String caption);

    void setMainComponent(Component component);

    void addFilterComponent(Component component);

    void addToolbarComponent(Component component);

    void showForm(CrudOperation operation, Component form);

    void hideForm();

}
