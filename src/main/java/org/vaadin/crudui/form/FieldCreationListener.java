package org.vaadin.crudui.form;

import com.vaadin.data.HasValue;

import java.io.Serializable;

/**
 * @author Alejandro Duarte.
 */
@FunctionalInterface
public interface FieldCreationListener extends Serializable {

    void fieldCreated(HasValue field);

}
