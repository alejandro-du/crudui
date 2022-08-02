package org.vaadin.crudui.form;

import java.io.Serializable;

import com.vaadin.flow.component.HasValueAndElement;

/**
 * @author Alejandro Duarte.
 */
@FunctionalInterface
public interface FieldProvider<C extends HasValueAndElement, T> extends Serializable {

    C buildField(T t);

}
