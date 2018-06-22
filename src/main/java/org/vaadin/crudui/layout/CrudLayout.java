package org.vaadin.crudui.layout;

import com.vaadin.flow.component.Component;
import org.vaadin.crudui.crud.CrudOperation;

/**
 * @author Alejandro Duarte
 */
public interface CrudLayout {

    void setMainComponent(Component component);

    void addFilterComponent(Component component);

    void addToolbarComponent(Component component);

    void showForm(CrudOperation operation, Component form);

    void hideForm();

}
