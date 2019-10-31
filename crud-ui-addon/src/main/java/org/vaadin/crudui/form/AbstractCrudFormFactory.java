package org.vaadin.crudui.form;

import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.util.SharedUtil;
import org.vaadin.crudui.crud.CrudOperation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Alejandro Duarte
 */
public abstract class AbstractCrudFormFactory<T> implements CrudFormFactory<T> {

    public static final String GENERATED_PROPERTY_NAME_PREFIX = "--";
    protected final Class<T> beanType;
    protected final PropertySet<T> propertySet;
    protected Consumer<Exception> errorListener;
    private Map<CrudOperation, CrudFormConfiguration<T>> configurationMap = new HashMap<>();

    public AbstractCrudFormFactory(Class<T> beanType) {
        this.beanType = beanType;
        propertySet = BeanPropertySet.get(beanType);
    }

    protected CrudFormConfiguration<T> getConfiguration(CrudOperation operation) {
        configurationMap.putIfAbsent(operation, new CrudFormConfiguration<>());
        return configurationMap.get(operation);
    }

    @Override
    public Property<T, ?> getProperty(CrudOperation operation,
            String propertyName) {
        return getConfiguration(operation).getPropertiesMap().get(propertyName);
    }

    @Override
    public List<Property<T, ?>> getProperty(String propertyName) {
        return CrudOperation.stream()
                .map(operation -> getConfiguration(operation).getPropertiesMap().get(propertyName))
                .collect(Collectors.toList());
    }

    @Override
    public <V> Property<T, V> addProperty(CrudOperation operation,
            Class<V> type, ValueProvider<T, V> getter,
            Setter<T, V> setter) {

        Property<T, V> property = new Property<>(type, getter, setter);
        String getterName = getter.getClass().getName();
        getConfiguration(operation).getPropertiesMap().put(GENERATED_PROPERTY_NAME_PREFIX + getterName, property);

        return property;
    }

    @Override
    public <V> Map<CrudOperation, Property<T, V>> addProperty(Class<V> type,
            ValueProvider<T, V> getter, Setter<T, V> setter) {
        HashMap<CrudOperation, Property<T, V>> properties = new HashMap<>();

        CrudOperation.stream().forEach(operation -> {
            Property<T, V> property = addProperty(operation, type, getter,
                    setter);
            properties.put(operation, property);
        });

        return properties;
    }

    @Override
    public <V> AbstractCrudFormFactory<T> addProperty(CrudOperation operation,
            String propertyName) {

        PropertyDefinition<T, V> definition =
                (PropertyDefinition<T, V>) propertySet.getProperty(propertyName).get();
        Class<V> type = (Class<V>) propertySet.getProperty(propertyName).get().getType();
        Property<T, V> property = new Property<>(type, definition.getGetter(),
                definition.getSetter().get());
        property.setFieldCaption(SharedUtil.propertyIdToHumanFriendly(propertyName));
        getConfiguration(operation).getPropertiesMap().put(propertyName, property);

        return this;
    }

    @Override
    public AbstractCrudFormFactory<T> addProperty(String propertyName) {
        CrudOperation.stream().forEach(operation -> addProperty(operation, propertyName));
        return this;
    }

    @Override
    public AbstractCrudFormFactory<T> setProperties(CrudOperation operation, String... propertyNames) {
        getConfiguration(operation).getPropertiesMap().clear();
        Arrays.stream(propertyNames).forEach(
                propertyName -> addProperty(operation, propertyName));

        return this;
    }

    @Override
    public AbstractCrudFormFactory<T> setProperties(String... propertyNames) {
        CrudOperation.stream().forEach(
                operation -> setProperties(operation, propertyNames));

        return this;
    }

    @Override
    public AbstractCrudFormFactory<T> setUseBeanValidation(CrudOperation operation, boolean useBeanValidation) {
        getConfiguration(operation).setUseBeanValidation(useBeanValidation);
        return this;
    }

    @Override
    public AbstractCrudFormFactory<T> setUseBeanValidation(boolean useBeanValidation) {
        CrudOperation.stream().forEach(
                operation -> setUseBeanValidation(operation, useBeanValidation));

        return this;
    }

    @Override
    public AbstractCrudFormFactory<T> setErrorListener(Consumer<Exception> errorListener) {
        this.errorListener = errorListener;
        return this;
    }

}
