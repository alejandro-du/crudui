package org.vaadin.crudui.crud;

import com.vaadin.ui.Component;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.layout.CrudLayout;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Alejandro Duarte
 */
public interface CrudComponent<T> extends Component {

    void setAddOperationVisible(boolean visible);

    void setUpdateOperationVisible(boolean visible);

    void setDeleteOperationVisible(boolean visible);

    void setFindAllOperationVisible(boolean visible);

    CrudFormFactory<T> getCrudFormFactory();

    void setCrudFormFactory(CrudFormFactory<T> crudFormFactory);

    void setFindAllOperation(Supplier<Collection<T>> findAllOperation);

    void setAddOperation(Consumer<T> addOperation);

    void setUpdateOperation(Consumer<T> updateOperation);

    void setDeleteOperation(Consumer<T> deleteOperation);

    void setOperations(Supplier<Collection<T>> findAllOperation, Consumer<T> addOperation, Consumer<T> updateOperation, Consumer<T> deleteOperation);

    void setCrudListener(CrudListener<T> crudListener);

    CrudLayout getCrudLayout();

}
