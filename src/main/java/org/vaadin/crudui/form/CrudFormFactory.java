package org.vaadin.crudui.form;

import com.vaadin.ui.Component;

import java.io.Serializable;

/**
 * @author Alejandro Duarte
 */
public interface CrudFormFactory<T> extends Serializable {

    Component buildNewForm(T domainObject, CrudFormConfiguration configuration);

}
