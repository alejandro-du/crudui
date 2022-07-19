package org.vaadin.crudui.form;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.function.SerializableConsumer;
import org.vaadin.crudui.crud.CrudOperation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alejandro Duarte
 */
public abstract class AbstractCrudFormFactory<T> implements CrudFormFactory<T> {

    protected Map<CrudOperation, CrudFormConfiguration> configurations = new HashMap<>();

    protected SerializableConsumer<Exception> errorListener;

    protected boolean showNotifications;

    @Override
    public void setVisibleProperties(CrudOperation operation, String... properties) {
        getConfiguration(operation).setVisibleProperties(Arrays.asList(properties));
    }

    @Override
    public void setVisibleProperties(String... properties) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setVisibleProperties(operation, properties));
    }

    @Override
    public void setDisabledProperties(CrudOperation operation, String... properties) {
        getConfiguration(operation).setDisabledProperties(Arrays.asList(properties));
    }

    @Override
    public void setDisabledProperties(String... properties) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setDisabledProperties(operation, properties));
    }

    @Override
    public void setFieldCaptions(CrudOperation operation, String... captions) {
        getConfiguration(operation).setFieldCaptions(Arrays.asList(captions));
    }

    @Override
    public void setFieldCaptions(String... captions) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setFieldCaptions(operation, captions));
    }

    @Override
    public void setFieldType(CrudOperation operation, String property, Class<? extends HasValueAndElement<?, ?>> type) {
        getConfiguration(operation).getFieldTypes().put(property, type);
    }

    @Override
    public void setFieldType(String property, Class<? extends HasValueAndElement<?, ?>> type) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setFieldType(operation, property, type));
    }

    @Override
    public void setFieldCreationListener(CrudOperation operation, String property, FieldCreationListener listener) {
        getConfiguration(operation).getFieldCreationListeners().put(property, listener);
    }

    @Override
    public void setFieldCreationListener(String property, FieldCreationListener listener) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setFieldCreationListener(operation, property, listener));
    }

    @Override
    public void setFieldProvider(CrudOperation operation, String property, FieldProvider<?, ?> provider) {
        getConfiguration(operation).getFieldProviders().put(property, provider);
    }

    @Override
    public void setFieldProvider(String property, FieldProvider<?, ?> provider) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setFieldProvider(operation, property, provider));
    }

    @Override
    public void setConverter(CrudOperation operation, String property, Converter<?, ?> converter) {
        getConfiguration(operation).getConverters().put(property, converter);
    }

    @Override
    public void setConverter(String property, Converter<?, ?> converter) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setConverter(operation, property, converter));
    }

    @Override
    public void setUseBeanValidation(CrudOperation operation, boolean useBeanValidation) {
        getConfiguration(operation).setUseBeanValidation(useBeanValidation);
    }

    @Override
    public void setUseBeanValidation(boolean useBeanValidation) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setUseBeanValidation(operation, useBeanValidation));
    }

    @Override
    public void setShowNotifications(boolean showNotifications) {
        this.showNotifications = showNotifications;
    }

    public void showNotification(String text) {
      if (showNotifications) {
        Notification.show(text);
      }
    }

    @Override
    public void setErrorListener(SerializableConsumer<Exception> errorListener) {
        this.errorListener = errorListener;
    }

    protected CrudFormConfiguration getConfiguration(CrudOperation operation) {
        configurations.putIfAbsent(operation, new CrudFormConfiguration());
        return configurations.get(operation);
    }

}
