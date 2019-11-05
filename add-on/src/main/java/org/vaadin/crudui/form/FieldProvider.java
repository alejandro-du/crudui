package org.vaadin.crudui.form;

import java.io.Serializable;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValueAndElement;

/**
 * @author Alejandro Duarte.
 */
@FunctionalInterface
public interface FieldProvider<C extends Component, T> extends Serializable {

    HasValueAndElement buildField();

}
