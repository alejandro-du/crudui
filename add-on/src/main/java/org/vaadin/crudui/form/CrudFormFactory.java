package org.vaadin.crudui.form;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableSupplier;
import org.vaadin.crudui.crud.CrudOperation;

import java.io.Serializable;

/**
 * @author Alejandro Duarte
 */
public interface CrudFormFactory<T> extends Serializable {

    void setNewInstanceSupplier(SerializableSupplier<T> newInstanceSupplier);

    SerializableSupplier<T> getNewInstanceSupplier();

    Component buildNewForm(CrudOperation operation, T domainObject, boolean readOnly, ComponentEventListener<ClickEvent<Button>> cancelButtonClickListener, ComponentEventListener<ClickEvent<Button>> operationButtonClickListener);

    String buildCaption(CrudOperation operation, T domainObject);

    void setVisibleProperties(CrudOperation operation, String... properties);

    void setVisibleProperties(String... properties);

    void setDisabledProperties(CrudOperation operation, String... properties);

    void setDisabledProperties(String... properties);

    void setFieldCaptions(CrudOperation operation, String... captions);

    void setFieldCaptions(String... captions);

    void setFieldType(CrudOperation operation, String property, Class<? extends HasValueAndElement<?, ?>> type);

    void setFieldType(String property, Class<? extends HasValueAndElement<?, ?>> type);

    void setFieldCreationListener(CrudOperation operation, String property, FieldCreationListener listener);

    void setFieldCreationListener(String property, FieldCreationListener listener);

    void setFieldProvider(CrudOperation operation, String property, FieldProvider<?, ?> provider);

    void setFieldProvider(String property, FieldProvider<?, ?> provider);

    void setConverter(CrudOperation operation, String property, Converter<?, ?> converter);

    void setConverter(String property, Converter<?, ?> converter);

    void setUseBeanValidation(CrudOperation operation, boolean useBeanValidation);

    void setUseBeanValidation(boolean useBeanValidation);

    void setShowNotifications(boolean showNotifications);

    void setErrorListener(SerializableConsumer<Exception> errorListener);

    void showError(CrudOperation operation, Exception e);

}
