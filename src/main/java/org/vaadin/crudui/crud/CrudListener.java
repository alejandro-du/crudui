package org.vaadin.crudui.crud;

import java.io.Serializable;

import com.vaadin.data.provider.DataProvider;

/**
 * @author Alejandro Duarte
 */
public interface CrudListener<T> extends Serializable {

    DataProvider<T, ?> getDataProvider();

    T add(T domainObjectToAdd);

    T update(T domainObjectToUpdate);

    void delete(T domainObjectToDelete);

}
