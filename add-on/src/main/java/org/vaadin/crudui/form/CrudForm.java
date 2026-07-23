package org.vaadin.crudui.form;

public interface CrudForm<B> {

	B getValue();

	void setValue(B value);

	boolean isReadOnly();

	void setReadOnly(boolean readOnly);

	boolean isValid();

}
