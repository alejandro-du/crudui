package org.vaadin.crudui.form;

import com.vaadin.data.HasValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alejandro Duarte.
 */
public class CrudFormConfiguration implements Serializable {

    protected List<Object> visiblePropertyIds = new ArrayList<>();
    protected List<Object> disabledPropertyIds = new ArrayList<>();
    protected List<String> fieldCaptions = new ArrayList<>();
    protected Map<Object, Class<? extends HasValue>> fieldTypes = new HashMap<>();
    protected Map<Object, FieldCreationListener> fieldCreationListeners = new HashMap<>();
    protected Map<Object, FieldProvider> fieldProviders = new HashMap<>();

    public List<Object> getVisiblePropertyIds() {
        return visiblePropertyIds;
    }

    public void setVisiblePropertyIds(List<Object> visiblePropertyIds) {
        this.visiblePropertyIds = visiblePropertyIds;
    }

    public List<Object> getDisabledPropertyIds() {
        return disabledPropertyIds;
    }

    public void setDisabledPropertyIds(List<Object> disabledPropertyIds) {
        this.disabledPropertyIds = disabledPropertyIds;
    }

    public List<String> getFieldCaptions() {
        return fieldCaptions;
    }

    public void setFieldCaptions(List<String> fieldCaptions) {
        this.fieldCaptions = fieldCaptions;
    }

    public Map<Object, Class<? extends HasValue>> getFieldTypes() {
        return fieldTypes;
    }

    public void setFieldTypes(Map<Object, Class<? extends HasValue>> fieldTypes) {
        this.fieldTypes = fieldTypes;
    }

    public Map<Object, FieldCreationListener> getFieldCreationListeners() {
        return fieldCreationListeners;
    }

    public void setFieldCreationListeners(Map<Object, FieldCreationListener> fieldCreationListeners) {
        this.fieldCreationListeners = fieldCreationListeners;
    }

    public Map<Object, FieldProvider> getFieldProviders() {
        return fieldProviders;
    }

    public void setFieldProviders(Map<Object, FieldProvider> fieldProviders) {
        this.fieldProviders = fieldProviders;
    }

}
