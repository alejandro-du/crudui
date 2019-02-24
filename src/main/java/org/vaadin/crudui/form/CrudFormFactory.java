package org.vaadin.crudui.form;

import java.io.Serializable;
import java.util.function.Consumer;

import org.vaadin.crudui.crud.CrudOperation;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;

/**
 * @author Alejandro Duarte
 */
public interface CrudFormFactory<T> extends Serializable {

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

    void setUseBeanValidation(CrudOperation operation, boolean useBeanValidation);

    void setUseBeanValidation(boolean useBeanValidation);

    void setErrorListener(Consumer<Exception> errorListener);

    void showError(CrudOperation operation, Exception e);

}
