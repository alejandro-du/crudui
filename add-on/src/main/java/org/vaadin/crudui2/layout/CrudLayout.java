package org.vaadin.crudui2.layout;

import com.vaadin.flow.component.Component;

import org.vaadin.crudui2.form.CrudForm;
import org.vaadin.crudui2.list.CrudList;

public interface CrudLayout<B> {

	void setCrudList(CrudList<B> crudList);

	void showCrudForm(CrudForm<B> crudForm);

	void hideForm();

	void addCrudActionComponent(Component component);

	void addFormActionComponent(Component component);

	void setFormActionCaption(String caption);

	void addFilterComponent(Component component);

}
