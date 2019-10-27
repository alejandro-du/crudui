package org.vaadin.crudui.crud;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.layout.CrudLayout;

import java.util.Collections;

/**
 * @author Alejandro Duarte
 */
public abstract class AbstractCrud<BEAN_TYPE> extends Composite<VerticalLayout> implements Crud<BEAN_TYPE>, HasSize {

    protected Class<BEAN_TYPE> beanType;

    protected FindAllCrudOperationListener<BEAN_TYPE> findAllOperation = Collections::emptyList;
    protected AddOperationListener<BEAN_TYPE> addOperation = t -> null;
    protected UpdateOperationListener<BEAN_TYPE> updateOperation = t -> null;
    protected DeleteOperationListener<BEAN_TYPE> deleteOperation = t -> {
    };

    protected CrudLayout crudLayout;
    protected CrudFormFactory<BEAN_TYPE> crudFormFactory;

    public AbstractCrud(Class<BEAN_TYPE> beanType, CrudLayout crudLayout, CrudFormFactory<BEAN_TYPE> crudFormFactory,
            CrudListener<BEAN_TYPE> crudListener) {
        this.beanType = beanType;
        this.crudLayout = crudLayout;
        this.crudFormFactory = crudFormFactory;

        if (crudListener != null) {
            setCrudListener(crudListener);
        }
        getContent().add((Component) crudLayout);

        getContent().setPadding(false);
        getContent().setMargin(false);
        setSizeFull();
    }

    public CrudLayout getCrudLayout() {
        return crudLayout;
    }

    @Override
    public void setCrudFormFactory(CrudFormFactory<BEAN_TYPE> crudFormFactory) {
        this.crudFormFactory = crudFormFactory;
    }

    @Override
    public void setFindAllOperation(FindAllCrudOperationListener<BEAN_TYPE> findAllOperation) {
        this.findAllOperation = findAllOperation;
    }

    @Override
    public void setFindAllOperation(DataProvider<BEAN_TYPE, ?> dataProvider) {
        this.findAllOperation = (LazyFindAllCrudOperationListener<BEAN_TYPE>) () -> dataProvider;
    }

    @Override
    public void setAddOperation(AddOperationListener<BEAN_TYPE> addOperation) {
        this.addOperation = addOperation;
    }

    @Override
    public void setUpdateOperation(UpdateOperationListener<BEAN_TYPE> updateOperation) {
        this.updateOperation = updateOperation;
    }

    @Override
    public void setDeleteOperation(DeleteOperationListener<BEAN_TYPE> deleteOperation) {
        this.deleteOperation = deleteOperation;
    }

    @Override
    public void setOperations(FindAllCrudOperationListener<BEAN_TYPE> findAllOperation,
            AddOperationListener<BEAN_TYPE> addOperation, UpdateOperationListener<BEAN_TYPE> updateOperation,
            DeleteOperationListener<BEAN_TYPE> deleteOperation) {

        setFindAllOperation(findAllOperation);
        setAddOperation(addOperation);
        setUpdateOperation(updateOperation);
        setDeleteOperation(deleteOperation);
    }

    @Override
    public CrudFormFactory<BEAN_TYPE> getCrudFormFactory() {
        return crudFormFactory;
    }

    @Override
    public void setCrudListener(CrudListener<BEAN_TYPE> crudListener) {
        setAddOperation(crudListener::add);
        setUpdateOperation(crudListener::update);
        setDeleteOperation(crudListener::delete);

        if (LazyCrudListener.class.isAssignableFrom(crudListener.getClass())) {
            setFindAllOperation(
                    (LazyFindAllCrudOperationListener<BEAN_TYPE>) ((LazyCrudListener) crudListener)::getDataProvider);
        } else {
            setFindAllOperation(crudListener::findAll);
        }
    }

}
