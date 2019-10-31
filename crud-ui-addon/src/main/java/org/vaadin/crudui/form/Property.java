package org.vaadin.crudui.form;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.function.ValueProvider;

public class Property<T, V> {

    private final ValueProvider<T, V> getter;

    private final Setter<T, V> setter;

    private final Class<V> type;

    private String fieldCaption;

    private boolean disabled;

    private Class<? extends HasValueAndElement<?, ?>> fieldType;

    private FieldCreationListener fieldCreationListener;

    private FieldProvider<?, ?> fieldProvider;

    private Converter<?, V> converter;

    public Property(Class<V> type, ValueProvider<T, V> getter,
            Setter<T, V> setter) {

        this.type = type;
        this.getter = getter;
        this.setter = setter;
    }

    public ValueProvider<T, V> getGetter() {
        return getter;
    }

    public Setter<T, V> getSetter() {
        return setter;
    }

    public Class<?> getType() {
        return type;
    }

    public String getFieldCaption() {
        return fieldCaption;
    }

    public Property<T, V> setFieldCaption(String fieldCaption) {
        this.fieldCaption = fieldCaption;
        return this;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public Property<T, V> setDisabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public Class<? extends HasValueAndElement<?, ?>> getFieldType() {
        return fieldType;
    }

    public Property<T, V> setFieldType(Class<? extends HasValueAndElement<?, ?>> fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public FieldCreationListener getFieldCreationListener() {
        return fieldCreationListener;
    }

    public Property<T, V> setFieldCreationListener(FieldCreationListener fieldCreationListener) {
        this.fieldCreationListener = fieldCreationListener;
        return this;
    }

    public FieldProvider<?, ?> getFieldProvider() {
        return fieldProvider;
    }

    public Property<T, V> setFieldProvider(FieldProvider<?, ?> fieldProvider) {
        this.fieldProvider = fieldProvider;
        return this;
    }

    public Converter<?, V> getConverter() {
        return converter;
    }

    public Property<T, V> setConverter(Converter<?, V> converter) {
        this.converter = converter;
        return this;
    }

}
