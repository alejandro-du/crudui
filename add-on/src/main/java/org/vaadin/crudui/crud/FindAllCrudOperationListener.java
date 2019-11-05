package org.vaadin.crudui.crud;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Alejandro Duarte.
 */
@FunctionalInterface
public interface FindAllCrudOperationListener<T> extends Serializable {

    Collection<T> findAll();

}
