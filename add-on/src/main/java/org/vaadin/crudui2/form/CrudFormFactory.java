package org.vaadin.crudui2.form;

@FunctionalInterface
public interface CrudFormFactory<B> {

	CrudForm<B, ?> build(B bean);

}
