package org.vaadin.crudui.crud;

import com.vaadin.ui.Composite;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.layout.CrudLayout;

import java.util.Collections;

/**
 * @author Alejandro Duarte
 */
public abstract class AbstractCrud<T> extends Composite implements Crud<T> {

    protected Class<T> domainType;

    protected FindAllCrudOperationListener<T> findAllOperation = () -> Collections.emptyList();
    protected AddOperationListener<T> addOperation = t -> null;
    protected UpdateOperationListener<T> updateOperation = t -> null;
    protected DeleteOperationListener<T> deleteOperation = t -> { };

    protected CrudLayout crudLayout;
    protected CrudFormFactory<T> crudFormFactory;

    public AbstractCrud(Class<T> domainType, CrudLayout crudLayout, CrudFormFactory<T> crudFormFactory, CrudListener<T> crudListener) {
        this.domainType = domainType;
        this.crudLayout = crudLayout;
        this.crudFormFactory = crudFormFactory;

        if (crudListener != null) {
            setCrudListener(crudListener);
        }

        setCompositionRoot(crudLayout);
        setSizeFull();
    }

    @Override
    public void setCaption(String caption) {
        crudLayout.setCaption(caption);
    }

    public CrudLayout getCrudLayout() {
        return crudLayout;
    }

    @Override
    public void setCrudFormFactory(CrudFormFactory<T> crudFormFactory) {
        this.crudFormFactory = crudFormFactory;
    }

    @Override
    public void setFindAllOperation(FindAllCrudOperationListener<T> findAllOperation) {
        this.findAllOperation = findAllOperation;
    }

    @Override
    public void setAddOperation(AddOperationListener<T> addOperation) {
        this.addOperation = addOperation;
    }

    @Override
    public void setUpdateOperation(UpdateOperationListener<T> updateOperation) {
        this.updateOperation = updateOperation;
    }

    @Override
    public void setDeleteOperation(DeleteOperationListener<T> deleteOperation) {
        this.deleteOperation = deleteOperation;
    }

    @Override
    public void setOperations(FindAllCrudOperationListener<T> findAllOperation, AddOperationListener<T> addOperation, UpdateOperationListener<T> updateOperation, DeleteOperationListener<T> deleteOperation) {
        setFindAllOperation(findAllOperation);
        setAddOperation(addOperation);
        setUpdateOperation(updateOperation);
        setDeleteOperation(deleteOperation);
    }

    @Override
    public CrudFormFactory<T> getCrudFormFactory() {
        return crudFormFactory;
    }

    @Override
    public void setCrudListener(CrudListener<T> crudListener) {
        setAddOperation(crudListener::add);
        setUpdateOperation(crudListener::update);
        setDeleteOperation(crudListener::delete);
        setFindAllOperation(crudListener::findAll);
    }

}
