package org.vaadin.crudui.crud;

import java.io.Serializable;

/**
 * @author watho.
 */
@FunctionalInterface
public interface PreCopyOperationListener<T> extends Serializable {

    T perform(T domainObject, Class<T> clazz);

}
