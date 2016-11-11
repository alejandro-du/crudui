package org.vaadin.crudui;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import org.vaadin.crudui.impl.form.VerticalCrudFormFactory;
import org.vaadin.crudui.impl.layout.WindowBasedCrudLayout;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Alejandro Duarte
 */
public abstract class AbstractCrudComponent<T> extends CustomComponent implements CrudComponent<T> {

    protected Class<T> domainType;

    protected List<Object> addFormVisiblePropertyIds;
    protected List<String> addFormFieldCaptions = new ArrayList<>();

    protected List<Object> updateFormVisiblePropertyIds;
    protected List<String> updateFormFieldCaptions = new ArrayList<>();
    protected List<Object> updateFormDisabledPropertyIds = new ArrayList<>();

    protected List<Object> deleteFormVisiblePropertyIds;
    protected List<String> deleteFormFieldCaptions = new ArrayList<>();

    protected Consumer<T> addOperation = t -> { };
    protected Consumer<T> updateOperation = t -> { };
    protected Consumer<T> deleteOperation = t -> { };
    protected Supplier<Collection<T>> findAllOperation = () -> Collections.emptyList();

    protected Map<Object, Class<? extends Field>> fieldTypes = new HashMap<>();
    protected Map<Object, Consumer<Field>> fieldCreationListeners = new HashMap<>();

    protected CrudLayout mainLayout;
    protected CrudFormFactory<T> crudFormFactory;

    public AbstractCrudComponent(Class<T> domainType) {
        this(domainType, new WindowBasedCrudLayout());
    }

    public AbstractCrudComponent(Class<T> domainType, CrudLayout mainLayout) {
        this.domainType = domainType;
        this.mainLayout = mainLayout;
        crudFormFactory = new VerticalCrudFormFactory<T>();
        addFormVisiblePropertyIds = discoverPropertyIds(domainType);
        updateFormVisiblePropertyIds = discoverPropertyIds(domainType);
        deleteFormVisiblePropertyIds = discoverPropertyIds(domainType);

        setCompositionRoot(mainLayout);
        setSizeFull();
    }

    protected List<Object> discoverPropertyIds(Class<T> domainType) {
        BeanItemContainer<T> propertyIdsHelper = new BeanItemContainer<T>(domainType);
        return new ArrayList<>(propertyIdsHelper.getContainerPropertyIds());
    }

    @Override
    public void setCaption(String caption) {
        mainLayout.setCaption(caption);
    }

    @Override
    public void setAddFormVisiblePropertyIds(Object... addFormVisiblePropertyIds) {
        this.addFormVisiblePropertyIds = Arrays.asList(addFormVisiblePropertyIds);
    }

    @Override
    public void setUpdateFormVisiblePropertyIds(Object... updateFormVisiblePropertyIds) {
        this.updateFormVisiblePropertyIds = Arrays.asList(updateFormVisiblePropertyIds);
    }

    @Override
    public void setDeleteFormVisiblePropertyIds(Object... deleteFormVisiblePropertyIds) {
        this.deleteFormVisiblePropertyIds = Arrays.asList(deleteFormVisiblePropertyIds);
    }

    @Override
    public void setVisiblePropertyIds(Object... visiblePropertyIds) {
        this.addFormVisiblePropertyIds =
                this.updateFormVisiblePropertyIds =
                        this.deleteFormVisiblePropertyIds =
                                Arrays.asList(visiblePropertyIds);
    }

    @Override
    public void setUpdateFormDisabledPropertyIds(Object... updateFormDisabledPropertyIds) {
        this.updateFormDisabledPropertyIds = Arrays.asList(updateFormDisabledPropertyIds);
    }

    @Override
    public void setAddFormFieldCaptions(String... addFormFieldCaptions) {
        this.addFormFieldCaptions = Arrays.asList(addFormFieldCaptions);
    }

    @Override
    public void setUpdateFormFieldCaptions(String... updateFormFieldCaptions) {
        this.updateFormFieldCaptions = Arrays.asList(updateFormFieldCaptions);
    }

    @Override
    public void setDeleteFormFieldCaptions(String... deleteFormFieldCaptions) {
        this.deleteFormFieldCaptions = Arrays.asList(deleteFormFieldCaptions);
    }

    @Override
    public CrudLayout getMainLayout() {
        return mainLayout;
    }

    @Override
    public void setCrudFormFactory(CrudFormFactory<T> crudFormFactory) {
        this.crudFormFactory = crudFormFactory;
    }

    @Override
    public void setAddOperation(Consumer<T> addOperation) {
        this.addOperation = addOperation;
    }

    @Override
    public void setUpdateOperation(Consumer<T> updateOperation) {
        this.updateOperation = updateOperation;
    }

    @Override
    public void setDeleteOperation(Consumer<T> deleteOperation) {
        this.deleteOperation = deleteOperation;
    }

    @Override
    public void setFindAllOperation(Supplier<Collection<T>> findAllOperation) {
        this.findAllOperation = findAllOperation;
    }

    @Override
    public void setOperations(Consumer<T> addOperation, Consumer<T> updateOperation, Consumer<T> deleteOperation, Supplier<Collection<T>> findAllOperation) {
        setAddOperation(addOperation);
        setUpdateOperation(updateOperation);
        setDeleteOperation(deleteOperation);
        setFindAllOperation(findAllOperation);
    }

    public CrudFormFactory<T> getCrudFormFactory() {
        return crudFormFactory;
    }

    @Override
    public void setCrudListener(CrudListener<T> crudListener) {
        setAddOperation(crudListener::add);
        setUpdateOperation(crudListener::update);
        setDeleteOperation(crudListener::delete);
        setFindAllOperation(crudListener::findAll);
    }

    @Override
    public void setFieldType(Object propertyId, Class<? extends Field> fieldClass) {
        fieldTypes.put(propertyId, fieldClass);
    }

    @Override
    public void setFieldCreationListener(Object propertyId, Consumer<Field> listener) {
        fieldCreationListeners.put(propertyId, listener);
    }

    protected List<CrudFieldConfiguration> buildFieldConfigurations(T domainObject, List<Object> visiblePropertyIds, List<Object> disabledPropertyIds, List<String> fieldCaptions, boolean readOnly) {
        ArrayList<CrudFieldConfiguration> fieldConfigurations = new ArrayList<>();

        for (int i = 0; i < visiblePropertyIds.size(); i++) {
            Object propertyId = visiblePropertyIds.get(i);
            Class fieldType = fieldTypes.get(propertyId);
            Consumer<Field> creationListener = fieldCreationListeners.get(propertyId);

            CrudFieldConfiguration fieldConfiguration = new CrudFieldConfiguration(
                    propertyId,
                    readOnly,
                    disabledPropertyIds == null || !disabledPropertyIds.contains(propertyId),
                    !fieldCaptions.isEmpty() ? fieldCaptions.get(i) : DefaultFieldFactory.createCaptionByPropertyId(propertyId),
                    fieldType != null ? fieldType : Field.class,
                    creationListener != null ? creationListener : field -> { }
            );

            fieldConfigurations.add(fieldConfiguration);
        }

        return fieldConfigurations;
    }

}
