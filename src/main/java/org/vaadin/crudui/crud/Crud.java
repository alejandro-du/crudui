package org.vaadin.crudui.crud;

import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.layout.CrudLayout;

import com.vaadin.flow.data.provider.DataProvider;

/**
 * @author Alejandro Duarte
 */
public interface Crud<T> {

    void setAddOperationVisible(boolean visible);

    void setCopyOperationVisible(boolean visible);

    void setUpdateOperationVisible(boolean visible);

    void setDeleteOperationVisible(boolean visible);

    void setFindAllOperationVisible(boolean visible);

    CrudFormFactory<T> getCrudFormFactory();

    void setCrudFormFactory(CrudFormFactory<T> crudFormFactory);

    void setFindAllOperation(FindAllCrudOperationListener<T> findAllOperation);

    void setFindAllOperation(DataProvider<T, Void> dataProvider);

    void setAddOperation(AddOperationListener<T> addOperation);

    void setPreCopyOperation(PreCopyOperationListener<T> preCopyOperation);

    void setUpdateOperation(UpdateOperationListener<T> updateOperation);

    void setDeleteOperation(DeleteOperationListener<T> deleteOperation);

    void setOperations(FindAllCrudOperationListener<T> findAllOperation, AddOperationListener<T> addOperation,
	    PreCopyOperationListener<T> preCopyOperation, UpdateOperationListener<T> updateOperation,
	    DeleteOperationListener<T> deleteOperation);

    void setCrudListener(CrudListener<T> crudListener);

    CrudLayout getCrudLayout();

}
