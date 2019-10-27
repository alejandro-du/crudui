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
public abstract class AbstractCrudFormFactory<BEAN_TYPE> implements CrudFormFactory<BEAN_TYPE> {

    public static final String GENERATED_PROPERTY_NAME_PREFIX = "--";
    protected final Class<BEAN_TYPE> beanType;
    protected final PropertySet<BEAN_TYPE> propertySet;
    protected Consumer<Exception> errorListener;
    private Map<CrudOperation, CrudFormConfiguration<BEAN_TYPE>> configurationMap = new HashMap<>();

    public AbstractCrudFormFactory(Class<BEAN_TYPE> beanType) {
        this.beanType = beanType;
        propertySet = BeanPropertySet.get(beanType);
    }

    protected CrudFormConfiguration<BEAN_TYPE> getConfiguration(CrudOperation operation) {
        configurationMap.putIfAbsent(operation, new CrudFormConfiguration<>());
        return configurationMap.get(operation);
    }

    @Override
    public Property<BEAN_TYPE, ?> getProperty(CrudOperation operation,
            String propertyName) {
        return getConfiguration(operation).getPropertiesMap().get(propertyName);
    }

    @Override
    public List<Property<BEAN_TYPE, ?>> getProperties(String propertyName) {
        return CrudOperation.stream()
                .map(operation -> getConfiguration(operation).getPropertiesMap().get(propertyName))
                .collect(Collectors.toList());
    }

    @Override
    public <PROPERTY_TYPE> Property<BEAN_TYPE, PROPERTY_TYPE> addProperty(CrudOperation operation,
            Class<PROPERTY_TYPE> type, ValueProvider<BEAN_TYPE, PROPERTY_TYPE> getter,
            Setter<BEAN_TYPE, PROPERTY_TYPE> setter) {

        Property<BEAN_TYPE, PROPERTY_TYPE> property = new Property<>(type, getter, setter);
        String getterName = getter.getClass().getName();
        getConfiguration(operation).getPropertiesMap().put(GENERATED_PROPERTY_NAME_PREFIX + getterName, property);

        return property;
    }

    @Override
    public <PROPERTY_TYPE> Map<CrudOperation, Property<BEAN_TYPE, PROPERTY_TYPE>> addProperty(Class<PROPERTY_TYPE> type,
            ValueProvider<BEAN_TYPE, PROPERTY_TYPE> getter, Setter<BEAN_TYPE, PROPERTY_TYPE> setter) {
        HashMap<CrudOperation, Property<BEAN_TYPE, PROPERTY_TYPE>> properties = new HashMap<>();

        CrudOperation.stream().forEach(operation -> {
            Property<BEAN_TYPE, PROPERTY_TYPE> property = addProperty(operation, type, getter,
                    setter);
            properties.put(operation, property);
        });

        return properties;
    }

    @Override
    public <PROPERTY_TYPE> AbstractCrudFormFactory<BEAN_TYPE> addProperty(CrudOperation operation,
            String propertyName) {

        PropertyDefinition<BEAN_TYPE, PROPERTY_TYPE> definition =
                (PropertyDefinition<BEAN_TYPE, PROPERTY_TYPE>) propertySet.getProperty(propertyName).get();
        Class<PROPERTY_TYPE> type = (Class<PROPERTY_TYPE>) propertySet.getProperty(propertyName).get().getType();
        Property<BEAN_TYPE, PROPERTY_TYPE> property = new Property<>(type, definition.getGetter(),
                definition.getSetter().get());
        property.setFieldCaption(SharedUtil.propertyIdToHumanFriendly(propertyName));
        getConfiguration(operation).getPropertiesMap().put(propertyName, property);

        return this;
    }

    @Override
    public AbstractCrudFormFactory<BEAN_TYPE> addProperty(String propertyName) {
        CrudOperation.stream().forEach(operation -> addProperty(operation, propertyName));
        return this;
    }

    @Override
    public AbstractCrudFormFactory<BEAN_TYPE> setProperties(CrudOperation operation, String... propertyNames) {
        getConfiguration(operation).getPropertiesMap().clear();
        Arrays.stream(propertyNames).forEach(
                propertyName -> addProperty(operation, propertyName));

        return this;
    }

    @Override
    public AbstractCrudFormFactory<BEAN_TYPE> setProperties(String... propertyNames) {
        CrudOperation.stream().forEach(
                operation -> setProperties(operation, propertyNames));

        return this;
    }

    @Override
    public AbstractCrudFormFactory<BEAN_TYPE> setUseBeanValidation(CrudOperation operation, boolean useBeanValidation) {
        getConfiguration(operation).setUseBeanValidation(useBeanValidation);
        return this;
    }

    @Override
    public AbstractCrudFormFactory<BEAN_TYPE> setUseBeanValidation(boolean useBeanValidation) {
        CrudOperation.stream().forEach(
                operation -> setUseBeanValidation(operation, useBeanValidation));

        return this;
    }

    @Override
    public AbstractCrudFormFactory<BEAN_TYPE> setErrorListener(Consumer<Exception> errorListener) {
        this.errorListener = errorListener;
        return this;
    }

}
