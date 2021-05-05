package org.vaadin.crudui.crud;

import java.io.Serializable;

/**
 * @author Alejandro Duarte.
 */
@FunctionalInterface
public interface UpdateOperationListener<T> extends Serializable {

    T perform(T domainObject);

}
