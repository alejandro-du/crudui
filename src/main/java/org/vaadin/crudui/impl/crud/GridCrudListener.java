package org.vaadin.crudui.impl.crud;

import org.vaadin.crudui.CrudListener;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public interface GridCrudListener<T> extends CrudListener<T> {
    Collection<T> findAll();
}
