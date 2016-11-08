package org.vaadin.crudui;

import com.vaadin.ui.Component;

import java.util.function.Consumer;

/**
 * @author Alejandro Duarte
 */
public interface CrudComponent<T> extends Component {

    void setAddOptionVisible(boolean visible);

    void setEditOptionVisible(boolean visible);

    void setDeleteOptionVisible(boolean visible);

    void setNewFormVisiblePropertyIds(Object... newFormVisiblePropertyIds);

    void setEditFormVisiblePropertyIds(Object... editFormVisiblePropertyIds);

    void setDeleteFormVisiblePropertyIds(Object... deleteFormVisiblePropertyIds);

    void setEditFormDisabledPropertyIds(Object... editFormDisabledPropertyIds);

    void setNewFormFieldCaptions(String... newFormFieldCaptions);

    void setEditFormFieldCaptions(String... editFormFieldCaptions);

    void setDeleteFormFieldCaptions(String... deleteFormFieldCaptions);

    CrudLayout getMainLayout();

    void setCrudFormBuilder(CrudFormBuilder<T> crudForm);

    void setOperations(Consumer<T> add, Consumer<T> update, Consumer<T> delete);

    void setAddOperation(Consumer<T> add);

    void setUpdateOperation(Consumer<T> update);

    void setDeleteOperation(Consumer<T> delete);

    void setCrudListener(CrudListener<T> crudListener);

}
