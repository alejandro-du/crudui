package org.vaadin.crudui.form;

import com.vaadin.ui.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Alejandro Duarte.
 */
public class CrudFormConfiguration {

    protected List<Object> visiblePropertyIds = new ArrayList<>();
    protected List<Object> disabledPropertyIds = new ArrayList<>();
    protected List<String> fieldCaptions = new ArrayList<>();
    protected Map<Object, Class<? extends Field>> fieldTypes = new HashMap<>();
    protected Map<Object, Consumer<Field>> fieldCreationListeners = new HashMap<>();
    protected Map<Object, Supplier<Field>> fieldProviders = new HashMap<>();

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

    public Map<Object, Class<? extends Field>> getFieldTypes() {
        return fieldTypes;
    }

    public void setFieldTypes(Map<Object, Class<? extends Field>> fieldTypes) {
        this.fieldTypes = fieldTypes;
    }

    public Map<Object, Consumer<Field>> getFieldCreationListeners() {
        return fieldCreationListeners;
    }

    public void setFieldCreationListeners(Map<Object, Consumer<Field>> fieldCreationListeners) {
        this.fieldCreationListeners = fieldCreationListeners;
    }

    public Map<Object, Supplier<Field>> getFieldProviders() {
        return fieldProviders;
    }

    public void setFieldProviders(Map<Object, Supplier<Field>> fieldProviders) {
        this.fieldProviders = fieldProviders;
    }

}
