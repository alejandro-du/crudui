package org.vaadin.crudui.form;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Alejandro Duarte.
 */
public class CrudFormConfiguration<T> implements Serializable {

    protected Map<String, Property<T, ?>> propertiesMap = new LinkedHashMap<>();
    protected boolean useBeanValidation;

    public Map<String, Property<T, ?>> getPropertiesMap() {
        return propertiesMap;
    }

    public void setPropertiesMap(Map<String, Property<T, ?>> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

    public boolean isUseBeanValidation() {
        return useBeanValidation;
    }

    public void setUseBeanValidation(boolean useBeanValidation) {
        this.useBeanValidation = useBeanValidation;
    }

}
