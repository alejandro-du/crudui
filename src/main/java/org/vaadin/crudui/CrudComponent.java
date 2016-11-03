package org.vaadin.crudui;

import com.vaadin.ui.Component;

/**
 * @author Alejandro Duarte
 */
public interface CrudComponent<T> extends Component {

    void showAllOptions();

    void showAddOption();

    void showEditOption();

    void showDeleteOption();

    void setNewFormVisiblePropertyIds(Object... newFormVisiblePropertyIds);

    void setEditFormVisiblePropertyIds(Object... editFormVisiblePropertyIds);

    void setDeleteFormVisiblePropertyIds(Object... deleteFormVisiblePropertyIds);

    void setEditFormDisabledPropertyIds(Object... editFormDisabledPropertyIds);

    void setNewFormFieldCaptions(String... newFormFieldCaptions);

    void setEditFormFieldCaptions(String... editFormFieldCaptions);

    void setDeleteFormFieldCaptions(String... deleteFormFieldCaptions);

    CrudLayout getMainLayout();

    void setCrudFormBuilder(CrudFormBuilder<T> crudForm);

}
