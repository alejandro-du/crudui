package org.vaadin.crudui.crud;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public interface CrudListener<T> extends Serializable {

    Collection<T> findAll();

    T add(T t);

    T update(T t);

    void delete(T t);

}
