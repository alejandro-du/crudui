package org.vaadin.crudui.form;

import com.vaadin.v7.ui.Field;

import java.io.Serializable;

/**
 * @author Alejandro Duarte.
 */
@FunctionalInterface
public interface FieldProvider extends Serializable {

    Field buildField();

}
