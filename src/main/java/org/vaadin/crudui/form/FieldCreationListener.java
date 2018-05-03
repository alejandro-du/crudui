package org.vaadin.crudui.form;

import java.io.Serializable;

import com.vaadin.flow.component.HasValue;

/**
 * @author Alejandro Duarte.
 */
@FunctionalInterface
public interface FieldCreationListener extends Serializable {

    void fieldCreated(HasValue field);

}
