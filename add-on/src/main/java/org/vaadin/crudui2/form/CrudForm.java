package org.vaadin.crudui2.form;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;

public class CrudForm<B> extends CustomField<B> {

	private Binder<B> binder;
	private FormLayout formLayout;

	public CrudForm(Class<B> beanType) {
		this(beanType, false);
	}

	public CrudForm(Class<B> domainType, boolean useBeanValidation) {
		if (useBeanValidation) {
			binder = new BeanValidationBinder<>(domainType);
		} else {
			binder = new Binder<>(domainType);
		}

		formLayout = new FormLayout();
		add(formLayout);
	}

	@Override
	protected B generateModelValue() {
		return binder.getBean();
	}

	@Override
	protected void setPresentationValue(B newPresentationValue) {
		binder.setBean(newPresentationValue);
	}

	public boolean isValid() {
		return binder.isValid();
	}

	public <C extends AbstractField<C, V>, V> void add(AbstractField<C, V> field,
			ValueProvider<B, V> getter, Setter<B, V> setter) {
		binder.forField(field).bind(getter, setter);
		formLayout.add(field);
	}

	public <C extends AbstractField<C, V>, V> void add(AbstractField<C, V> field,
			String javaPropertyName) {
		binder.forField(field).bind(javaPropertyName);
		formLayout.add(field);
	}

}
