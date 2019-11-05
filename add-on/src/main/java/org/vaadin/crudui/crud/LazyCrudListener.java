package org.vaadin.crudui.crud;

import com.vaadin.flow.data.provider.DataProvider;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public interface LazyCrudListener<T> extends CrudListener<T> {

    default Collection<T> findAll() {
        throw new UnsupportedOperationException("Use fetch and count methods instead.");
    }

    DataProvider<T, Void> getDataProvider();

}
