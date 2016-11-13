package org.vaadin.crudui.crud;

import com.vaadin.ui.CustomComponent;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.form.impl.VerticalCrudFormFactory;
import org.vaadin.crudui.layout.CrudLayout;
import org.vaadin.crudui.layout.impl.WindowBasedCrudLayout;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Alejandro Duarte
 */
public abstract class AbstractCrudComponent<T> extends CustomComponent implements CrudComponent<T> {

    protected Class<T> domainType;

    protected Consumer<T> addOperation = t -> { };
    protected Consumer<T> updateOperation = t -> { };
    protected Consumer<T> deleteOperation = t -> { };

    protected Supplier<Collection<T>> findAllOperation = () -> Collections.emptyList();

    protected CrudLayout crudLayout;
    protected CrudFormFactory<T> crudFormFactory;

    public AbstractCrudComponent(Class<T> domainType) {
        this(domainType, new WindowBasedCrudLayout());
    }

    public AbstractCrudComponent(Class<T> domainType, CrudLayout crudLayout) {
        this.domainType = domainType;
        this.crudLayout = crudLayout;
        crudFormFactory = new VerticalCrudFormFactory<T>(domainType);

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
    public void setAddOperation(Consumer<T> addOperation) {
        this.addOperation = addOperation;
    }

    @Override
    public void setUpdateOperation(Consumer<T> updateOperation) {
        this.updateOperation = updateOperation;
    }

    @Override
    public void setDeleteOperation(Consumer<T> deleteOperation) {
        this.deleteOperation = deleteOperation;
    }

    @Override
    public void setFindAllOperation(Supplier<Collection<T>> findAllOperation) {
        this.findAllOperation = findAllOperation;
    }

    @Override
    public void setOperations(Supplier<Collection<T>> findAllOperation, Consumer<T> addOperation, Consumer<T> updateOperation, Consumer<T> deleteOperation) {
        setFindAllOperation(findAllOperation);
        setAddOperation(addOperation);
        setUpdateOperation(updateOperation);
        setDeleteOperation(deleteOperation);
    }

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
