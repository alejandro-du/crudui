package org.vaadin.crudui.crud;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public interface CrudListener<T> extends Serializable {

    Collection<T> findAll();

    void add(T domainObjectToAdd);

    void update(T domainObjectToUpdate);

    void delete(T domainObjectToDelete);

}
