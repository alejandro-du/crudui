package org.vaadin.crudui2.form.field.provider;

import java.io.Serializable;

import com.vaadin.flow.component.AbstractField;

@FunctionalInterface
public interface FieldProvider<C extends AbstractField<?, V>, V> extends Serializable {

	C buildField();

}
