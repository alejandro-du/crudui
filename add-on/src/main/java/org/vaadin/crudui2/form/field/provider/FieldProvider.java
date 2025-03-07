package org.vaadin.crudui2.form.field.provider;

import java.io.Serializable;

import com.vaadin.flow.component.AbstractField;

@FunctionalInterface
public interface FieldProvider<B, C extends AbstractField<?, ?>> extends Serializable {

	C buildField(B bean);

}
