package org.vaadin.crudui.form;

import com.vaadin.ui.Field;
import org.vaadin.crudui.CrudOperation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Alejandro Duarte
 */
public abstract class AbstractCrudFormFactory<T> implements CrudFormFactory<T> {

    protected Map<CrudOperation, CrudFormConfiguration> configurations = new HashMap<>();

    @Override
    public void setVisiblePropertyIds(CrudOperation operation, Object... propertyIds) {
        getConfiguration(operation).setVisiblePropertyIds(Arrays.asList(propertyIds));
    }

    @Override
    public void setVisiblePropertyIds(Object... propertyIds) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setVisiblePropertyIds(operation, propertyIds));
    }

    @Override
    public void setDisabledPropertyIds(CrudOperation operation, Object... propertyIds) {
        getConfiguration(operation).setDisabledPropertyIds(Arrays.asList(propertyIds));
    }

    @Override
    public void setDisabledPropertyIds(Object... propertyIds) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setDisabledPropertyIds(operation, propertyIds));
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
    public void setFieldType(CrudOperation operation, Object propertyId, Class<? extends Field> type) {
        getConfiguration(operation).getFieldTypes().put(propertyId, type);
    }

    @Override
    public void setFieldType(Object propertyId, Class<? extends Field> type) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setFieldType(operation, propertyId, type));
    }

    @Override
    public void setFieldCreationListener(CrudOperation operation, Object propertyId, Consumer<Field> listener) {
        getConfiguration(operation).getFieldCreationListeners().put(propertyId, listener);
    }

    @Override
    public void setFieldCreationListener(Object propertyId, Consumer<Field> listener) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setFieldCreationListener(operation, propertyId, listener));
    }

    @Override
    public void setFieldProvider(CrudOperation operation, Object propertyId, Supplier<Field> provider) {
        getConfiguration(operation).getFieldProviders().put(propertyId, provider);
    }

    @Override
    public void setFieldProvider(Object propertyId, Supplier<Field> provider) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setFieldProvider(operation, propertyId, provider));
    }

    protected CrudFormConfiguration getConfiguration(CrudOperation operation) {
        configurations.putIfAbsent(operation, new CrudFormConfiguration());
        return configurations.get(operation);
    }

}
