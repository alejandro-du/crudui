package org.vaadin.crudui.crud;

import com.vaadin.flow.data.provider.DataProvider;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.layout.CrudLayout;

/**
 * @author Alejandro Duarte
 */
public interface Crud<T> {

    void setAddOperationVisible(boolean visible);

    void setUpdateOperationVisible(boolean visible);

    void setDeleteOperationVisible(boolean visible);

    void setFindAllOperationVisible(boolean visible);

    CrudFormFactory<T> getCrudFormFactory();

    void setCrudFormFactory(CrudFormFactory<T> crudFormFactory);

    void setFindAllOperation(FindAllCrudOperationListener<T> findAllOperation);

    void setFindAllOperation(DataProvider<T, ?> dataProvider);

    void setAddOperation(AddOperationListener<T> addOperation);

    void setUpdateOperation(UpdateOperationListener<T> updateOperation);

    void setDeleteOperation(DeleteOperationListener<T> deleteOperation);

    void setOperations(FindAllCrudOperationListener<T> findAllOperation, AddOperationListener<T> addOperation, UpdateOperationListener<T> updateOperation, DeleteOperationListener<T> deleteOperation);

    void setCrudListener(CrudListener<T> crudListener);

    CrudLayout getCrudLayout();

}
