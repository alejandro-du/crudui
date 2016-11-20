package org.vaadin.crudui.form;

import com.vaadin.ui.Field;

import java.io.Serializable;

/**
 * @author Alejandro Duarte.
 */
@FunctionalInterface
public interface FieldProvider extends Serializable {

    Field buildField();

}
