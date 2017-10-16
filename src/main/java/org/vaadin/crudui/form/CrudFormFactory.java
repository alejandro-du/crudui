package org.vaadin.crudui.form;

import com.vaadin.data.HasValue;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.crudui.crud.CrudOperation;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author Alejandro Duarte
 */
public interface CrudFormFactory<T> extends Serializable {

    Component buildNewForm(CrudOperation operation, T domainObject, boolean readOnly, Button.ClickListener cancelButtonClickListener, Button.ClickListener operationButtonClickListener);

    void setVisibleProperties(CrudOperation operation, String... properties);

    void setVisibleProperties(String... properties);

    void setDisabledProperties(CrudOperation operation, String... properties);

    void setDisabledProperties(String... properties);

    void setFieldCaptions(CrudOperation operation, String... captions);

    void setFieldCaptions(String... captions);

    void setFieldType(CrudOperation operation, String property, Class<? extends HasValue> type);

    void setFieldType(String property, Class<? extends HasValue> type);

    void setFieldCreationListener(CrudOperation operation, String property, FieldCreationListener listener);

    void setFieldCreationListener(String property, FieldCreationListener listener);

    void setFieldProvider(CrudOperation operation, String property, FieldProvider provider);

    void setFieldProvider(String property, FieldProvider provider);

    void setUseBeanValidation(CrudOperation operation, boolean useBeanValidation);

    void setUseBeanValidation(boolean useBeanValidation);

    void setErrorListener(Consumer<Exception> errorListener);

    void showError(CrudOperation operation, Exception e);

}
