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
public interface CrudFormFactory<T> extends Serializable {

    Component buildNewForm(CrudOperation operation, T bean, boolean readOnly,
            ComponentEventListener<ClickEvent<Button>> cancelButtonClickListener,
            ComponentEventListener<ClickEvent<Button>> operationButtonClickListener);

    Property<T, ?> getProperty(CrudOperation operation, String propertyName);

    List<Property<T, ?>> getProperty(String propertyName);

    <V> Property<T, V> addProperty(CrudOperation operation, Class<V> type,
            ValueProvider<T, V> getter, Setter<T, V> setter);

    <V> Map<CrudOperation, ? extends Property<T, V>> addProperty(Class<V> type,
            ValueProvider<T, V> getter, Setter<T, V> setter);

    <V> AbstractCrudFormFactory<T> addProperty(CrudOperation operation, String propertyName);

    AbstractCrudFormFactory<T> addProperty(String propertyName);

    AbstractCrudFormFactory<T> setProperties(CrudOperation operation, String... propertyNames);

    AbstractCrudFormFactory<T> setProperties(String... propertyNames);

    AbstractCrudFormFactory<T> setUseBeanValidation(CrudOperation operation, boolean useBeanValidation);

    AbstractCrudFormFactory<T> setUseBeanValidation(boolean useBeanValidation);

    AbstractCrudFormFactory<T> setErrorListener(Consumer<Exception> errorListener);

    String buildCaption(CrudOperation operation, T bean);

    void showError(CrudOperation operation, Exception e);

}
