package org.vaadin.crudui.form;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import org.vaadin.crudui.crud.CrudOperation;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Alejandro Duarte
 */
public interface CrudFormFactory<BEAN_TYPE> extends Serializable {

    Component buildNewForm(CrudOperation operation, BEAN_TYPE bean, boolean readOnly,
            ComponentEventListener<ClickEvent<Button>> cancelButtonClickListener,
            ComponentEventListener<ClickEvent<Button>> operationButtonClickListener);

    Property<BEAN_TYPE, ?> getProperty(CrudOperation operation, String propertyName);

    List<Property<BEAN_TYPE, ?>> getProperties(String propertyName);

    <PROPERTY_TYPE> Property<BEAN_TYPE, PROPERTY_TYPE> addProperty(CrudOperation operation, Class<PROPERTY_TYPE> type,
            ValueProvider<BEAN_TYPE, PROPERTY_TYPE> getter, Setter<BEAN_TYPE, PROPERTY_TYPE> setter);

    <PROPERTY_TYPE> Map<CrudOperation, Property<BEAN_TYPE, PROPERTY_TYPE>> addProperty(Class<PROPERTY_TYPE> type,
            ValueProvider<BEAN_TYPE, PROPERTY_TYPE> getter, Setter<BEAN_TYPE, PROPERTY_TYPE> setter);

    <PROPERTY_TYPE> AbstractCrudFormFactory<BEAN_TYPE> addProperty(CrudOperation operation, String propertyName);

    AbstractCrudFormFactory<BEAN_TYPE> addProperty(String propertyName);

    AbstractCrudFormFactory<BEAN_TYPE> setProperties(CrudOperation operation, String... propertyNames);

    AbstractCrudFormFactory<BEAN_TYPE> setProperties(String... propertyNames);

    AbstractCrudFormFactory<BEAN_TYPE> setUseBeanValidation(CrudOperation operation, boolean useBeanValidation);

    AbstractCrudFormFactory<BEAN_TYPE> setUseBeanValidation(boolean useBeanValidation);

    AbstractCrudFormFactory<BEAN_TYPE> setErrorListener(Consumer<Exception> errorListener);

    String buildCaption(CrudOperation operation, BEAN_TYPE bean);

    void showError(CrudOperation operation, Exception e);

}
