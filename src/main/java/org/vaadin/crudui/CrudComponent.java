package org.vaadin.crudui;

import com.vaadin.ui.Component;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Alejandro Duarte
 */
public interface CrudComponent<T> extends Component {

    void setAddOptionVisible(boolean visible);

    void setUpdateOptionVisible(boolean visible);

    void setDeleteOptionVisible(boolean visible);

    void setFindAllOptionVisible(boolean visible);

    void setAddFormVisiblePropertyIds(Object... addFormVisiblePropertyIds);

    void setUpdateFormVisiblePropertyIds(Object... updateFormVisiblePropertyIds);

    void setDeleteFormVisiblePropertyIds(Object... deleteFormVisiblePropertyIds);

    void setVisiblePropertyIds(Object... visiblePropertyIds);

    void setUpdateFormDisabledPropertyIds(Object... updateFormDisabledPropertyIds);

    void setAddFormFieldCaptions(String... addFormFieldCaptions);

    void setUpdateFormFieldCaptions(String... updateFormFieldCaptions);

    void setDeleteFormFieldCaptions(String... deleteFormFieldCaptions);

    CrudLayout getMainLayout();

    void setCrudFormBuilder(CrudFormBuilder<T> crudFormBuilder);

    void setAddOperation(Consumer<T> addOperation);

    void setUpdateOperation(Consumer<T> updateOperation);

    void setDeleteOperation(Consumer<T> deleteOperation);

    void setFindAllOperation(Supplier<Collection<T>> findAllOperation);

    void setOperations(Consumer<T> addOperation, Consumer<T> updateOperation, Consumer<T> deleteOperation, Supplier<Collection<T>> findAllOperation);

    void setCrudListener(CrudListener<T> crudListener);

}
