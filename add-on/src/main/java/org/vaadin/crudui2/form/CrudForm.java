package org.vaadin.crudui2.form;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValueAndElement;

public interface CrudForm<B, C extends AbstractField<C, B>>
		extends HasValueAndElement<ComponentValueChangeEvent<C, B>, B> {

	boolean isValid();

}
