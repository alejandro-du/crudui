package org.vaadin.crudui.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.vaadin.crudui.CrudOperation;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Alejandro Duarte
 */
public interface CrudFormFactory<T> extends Serializable {

    Component buildNewForm(CrudOperation operation, T domainObject, boolean readOnly, Consumer<T> crudOperationListener);

    void setVisiblePropertyIds(CrudOperation operation, Object... propertyIds);

    void setVisiblePropertyIds(Object... propertyIds);

    void setDisabledPropertyIds(CrudOperation operation, Object... propertyIds);

    void setDisabledPropertyIds(Object... propertyIds);

    void setFieldCaptions(CrudOperation operation, String... captions);

    void setFieldCaptions(String... captions);

    void setFieldType(CrudOperation operation, Object propertyId, Class<? extends Field> type);

    void setFieldType(Object propertyId, Class<? extends Field> type);

    void setFieldCreationListener(CrudOperation operation, Object propertyId, Consumer<Field> listener);

    void setFieldCreationListener(Object propertyId, Consumer<Field> listener);

    void setFieldProvider(CrudOperation operation, Object propertyId, Supplier<Field> provider);

    void setFieldProvider(Object propertyId, Supplier<Field> provider);

}
