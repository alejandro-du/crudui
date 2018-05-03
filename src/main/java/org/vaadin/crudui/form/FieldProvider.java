package org.vaadin.crudui.form;

import java.io.Serializable;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;

/**
 * @author Alejandro Duarte.
 */
@FunctionalInterface
public interface FieldProvider<C extends Component, T> extends Serializable {

    HasValue<ComponentValueChangeEvent<C, T>, T> buildField();

}
