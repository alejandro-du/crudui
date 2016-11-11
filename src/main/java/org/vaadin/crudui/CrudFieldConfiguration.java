package org.vaadin.crudui;

import com.vaadin.data.Validator;
import com.vaadin.ui.Field;

import java.util.Collection;
import java.util.function.Consumer;

public class CrudFieldConfiguration {

    private Object propertyId;

    private boolean readOnly;

    private boolean enabled;

    private String caption;

    private Class<? extends Field> fieldType;

    private Consumer<Field> creationListener;

    private Collection<Validator> validators;

    public CrudFieldConfiguration(
            Object propertyId,
            boolean readOnly,
            boolean enabled,
            String caption,
            Class<? extends Field> fieldType,
            Consumer<Field> creationListener,
            Collection<Validator> validators
    ) {
        this.propertyId = propertyId;
        this.readOnly = readOnly;
        this.enabled = enabled;
        this.caption = caption;
        this.fieldType = fieldType;
        this.creationListener = creationListener;
        this.validators = validators;
    }

    public Object getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Object propertyId) {
        this.propertyId = propertyId;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Class<? extends Field> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<? extends Field> fieldType) {
        this.fieldType = fieldType;
    }

    public Consumer<Field> getCreationListener() {
        return creationListener;
    }

    public void setCreationListener(Consumer<Field> creationListener) {
        this.creationListener = creationListener;
    }

    public Collection<Validator> getValidators() {
        return validators;
    }

    public void setValidators(Collection<Validator> validators) {
        this.validators = validators;
    }

}