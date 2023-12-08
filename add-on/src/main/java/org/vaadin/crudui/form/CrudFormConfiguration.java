package org.vaadin.crudui.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.data.binder.Setter;

/**
 * @author Alejandro Duarte.
 */
public class CrudFormConfiguration implements Serializable {

    protected List<String> visibleProperties = new ArrayList<>();
    protected List<String> disabledProperties = new ArrayList<>();
    protected List<String> fieldCaptions = new ArrayList<>();
    protected Map<Object, Class<? extends HasValueAndElement<?, ?>>> fieldTypes = new HashMap<>();
    protected Map<Object, FieldCreationListener> fieldCreationListeners = new HashMap<>();
    protected Map<Object, FieldProvider<?, ?>> fieldProviders = new HashMap<>();
    protected Map<Object, Converter<?, ?>> converters = new HashMap<>();
    protected Map<Object, ValueProvider<?,?>> getters = new HashMap<>();
    protected Map<Object, Setter<?,?>> setters = new HashMap<>();
    protected Map<Object, Class<?>> propertyTypes = new HashMap<>();
    protected boolean useBeanValidation;

    public List<String> getVisibleProperties() {
        return visibleProperties;
    }

    public void setVisibleProperties(List<String> visibleProperties) {
        this.visibleProperties = visibleProperties;
    }

    public List<String> getDisabledProperties() {
        return disabledProperties;
    }

    public void setDisabledProperties(List<String> disabledProperties) {
        this.disabledProperties = disabledProperties;
    }

    public List<String> getFieldCaptions() {
        return fieldCaptions;
    }

    public void setFieldCaptions(List<String> fieldCaptions) {
        this.fieldCaptions = fieldCaptions;
    }

    public Map<Object, Class<? extends HasValueAndElement<?, ?>>> getFieldTypes() {
        return fieldTypes;
    }

    public void setFieldTypes(Map<Object, Class<? extends HasValueAndElement<?, ?>>> fieldTypes) {
        this.fieldTypes = fieldTypes;
    }

    public Map<Object, FieldCreationListener> getFieldCreationListeners() {
        return fieldCreationListeners;
    }

    public void setFieldCreationListeners(Map<Object, FieldCreationListener> fieldCreationListeners) {
        this.fieldCreationListeners = fieldCreationListeners;
    }

    public Map<Object, FieldProvider<?, ?>> getFieldProviders() {
        return fieldProviders;
    }

    public void setFieldProviders(Map<Object, FieldProvider<?, ?>> fieldProviders) {
        this.fieldProviders = fieldProviders;
    }

    public Map<Object, Converter<?, ?>> getConverters() {
        return converters;
    }

    public void setConverters(Map<Object, Converter<?, ?>> converters) {
        this.converters = converters;
    }

    public boolean isUseBeanValidation() {
        return useBeanValidation;
    }

    public void setUseBeanValidation(boolean useBeanValidation) {
        this.useBeanValidation = useBeanValidation;
    }

    public Map<Object, ValueProvider<?, ?>> getGetters() {
        return getters;
    }

    public void setGetters(Map<Object, ValueProvider<?, ?>> getters) {
        this.getters = getters;
    }

    public Map<Object, Setter<?, ?>> getSetters() {
        return setters;
    }

    public void setSetters(Map<Object, Setter<?, ?>> setters) {
        this.setters = setters;
    }

    public Map<Object, Class<?>> getPropertyTypes() {
        return propertyTypes;
    }

    public void setPropertyTypes(Map<Object, Class<?>> propertyTypes) {
        this.propertyTypes = propertyTypes;
    }

    
}
