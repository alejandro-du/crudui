package org.vaadin.crudui.form;

import com.vaadin.data.HasValue;
import org.vaadin.crudui.crud.CrudOperation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    public void setFieldType(CrudOperation operation, Object propertyId, Class<? extends HasValue> type) {
        getConfiguration(operation).getFieldTypes().put(propertyId, type);
    }

    @Override
    public void setFieldType(Object propertyId, Class<? extends HasValue> type) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setFieldType(operation, propertyId, type));
    }

    @Override
    public void setFieldCreationListener(CrudOperation operation, Object propertyId, FieldCreationListener listener) {
        getConfiguration(operation).getFieldCreationListeners().put(propertyId, listener);
    }

    @Override
    public void setFieldCreationListener(Object propertyId, FieldCreationListener listener) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setFieldCreationListener(operation, propertyId, listener));
    }

    @Override
    public void setFieldProvider(CrudOperation operation, Object propertyId, FieldProvider provider) {
        getConfiguration(operation).getFieldProviders().put(propertyId, provider);
    }

    @Override
    public void setFieldProvider(Object propertyId, FieldProvider provider) {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setFieldProvider(operation, propertyId, provider));
    }

    protected CrudFormConfiguration getConfiguration(CrudOperation operation) {
        configurations.putIfAbsent(operation, new CrudFormConfiguration());
        return configurations.get(operation);
    }

}
