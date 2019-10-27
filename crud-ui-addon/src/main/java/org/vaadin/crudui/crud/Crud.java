package org.vaadin.crudui.crud;

import com.vaadin.flow.data.provider.DataProvider;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.layout.CrudLayout;

/**
 * @author Alejandro Duarte
 */
public interface Crud<BEAN_TYPE> {

    void setAddOperationVisible(boolean visible);

    void setUpdateOperationVisible(boolean visible);

    void setDeleteOperationVisible(boolean visible);

    void setFindAllOperationVisible(boolean visible);

    CrudFormFactory<BEAN_TYPE> getCrudFormFactory();

    void setCrudFormFactory(CrudFormFactory<BEAN_TYPE> crudFormFactory);

    void setFindAllOperation(FindAllCrudOperationListener<BEAN_TYPE> findAllOperation);

    void setFindAllOperation(DataProvider<BEAN_TYPE, ?> dataProvider);

    void setAddOperation(AddOperationListener<BEAN_TYPE> addOperation);

    void setUpdateOperation(UpdateOperationListener<BEAN_TYPE> updateOperation);

    void setDeleteOperation(DeleteOperationListener<BEAN_TYPE> deleteOperation);

    void setOperations(FindAllCrudOperationListener<BEAN_TYPE> findAllOperation,
            AddOperationListener<BEAN_TYPE> addOperation, UpdateOperationListener<BEAN_TYPE> updateOperation,
            DeleteOperationListener<BEAN_TYPE> deleteOperation);

    void setCrudListener(CrudListener<BEAN_TYPE> crudListener);

    CrudLayout getCrudLayout();

}
