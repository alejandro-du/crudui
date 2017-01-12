package org.vaadin.crudui.form;

import com.vaadin.data.HasValue;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.crudui.crud.CrudOperation;

import java.io.Serializable;

/**
 * @author Alejandro Duarte
 */
public interface CrudFormFactory<T> extends Serializable {

    Component buildNewForm(CrudOperation operation, T domainObject, boolean readOnly, Button.ClickListener cancelButtonClickListener, Button.ClickListener operationButtonClickListener);

    void setVisiblePropertyIds(CrudOperation operation, Object... propertyIds);

    void setVisiblePropertyIds(Object... propertyIds);

    void setDisabledPropertyIds(CrudOperation operation, Object... propertyIds);

    void setDisabledPropertyIds(Object... propertyIds);

    void setFieldCaptions(CrudOperation operation, String... captions);

    void setFieldCaptions(String... captions);

    void setFieldType(CrudOperation operation, Object propertyId, Class<? extends HasValue> type);

    void setFieldType(Object propertyId, Class<? extends HasValue> type);

    void setFieldCreationListener(CrudOperation operation, Object propertyId, FieldCreationListener listener);

    void setFieldCreationListener(Object propertyId, FieldCreationListener listener);

    void setFieldProvider(CrudOperation operation, Object propertyId, FieldProvider provider);

    void setFieldProvider(Object propertyId, FieldProvider provider);

}
