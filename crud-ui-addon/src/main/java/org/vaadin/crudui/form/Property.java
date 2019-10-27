package org.vaadin.crudui.form;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.function.ValueProvider;

public class Property<BEAN_TYPE, PROPERTY_TYPE> {

    private final ValueProvider<BEAN_TYPE, PROPERTY_TYPE> getter;

    private final Setter<BEAN_TYPE, PROPERTY_TYPE> setter;

    private final Class<PROPERTY_TYPE> type;

    private String fieldCaption;

    private boolean disabled;

    private Class<? extends HasValueAndElement<?, ?>> fieldType;

    private FieldCreationListener fieldCreationListener;

    private FieldProvider<?, ?> fieldProvider;

    private Converter<?, ?> converter;

    public Property(Class<PROPERTY_TYPE> type, ValueProvider<BEAN_TYPE, PROPERTY_TYPE> getter,
            Setter<BEAN_TYPE, PROPERTY_TYPE> setter) {

        this.type = type;
        this.getter = getter;
        this.setter = setter;
    }

    public ValueProvider<BEAN_TYPE, PROPERTY_TYPE> getGetter() {
        return getter;
    }

    public Setter<BEAN_TYPE, PROPERTY_TYPE> getSetter() {
        return setter;
    }

    public Class<?> getType() {
        return type;
    }

    public String getFieldCaption() {
        return fieldCaption;
    }

    public Property<BEAN_TYPE, PROPERTY_TYPE> setFieldCaption(String fieldCaption) {
        this.fieldCaption = fieldCaption;
        return this;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public Property<BEAN_TYPE, PROPERTY_TYPE> setDisabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public Class<? extends HasValueAndElement<?, ?>> getFieldType() {
        return fieldType;
    }

    public Property<BEAN_TYPE, PROPERTY_TYPE> setFieldType(Class<? extends HasValueAndElement<?, ?>> fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public FieldCreationListener getFieldCreationListener() {
        return fieldCreationListener;
    }

    public Property<BEAN_TYPE, PROPERTY_TYPE> setFieldCreationListener(FieldCreationListener fieldCreationListener) {
        this.fieldCreationListener = fieldCreationListener;
        return this;
    }

    public FieldProvider<?, ?> getFieldProvider() {
        return fieldProvider;
    }

    public Property<BEAN_TYPE, PROPERTY_TYPE> setFieldProvider(FieldProvider<?, ?> fieldProvider) {
        this.fieldProvider = fieldProvider;
        return this;
    }

    public Converter<?, ?> getConverter() {
        return converter;
    }

    public Property<BEAN_TYPE, PROPERTY_TYPE> setConverter(Converter<?, ?> converter) {
        this.converter = converter;
        return this;
    }

}
