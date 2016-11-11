package org.vaadin.crudui.impl.crud;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.crudui.AbstractCrudComponent;
import org.vaadin.crudui.CrudFieldConfiguration;
import org.vaadin.crudui.CrudFormConfiguration;
import org.vaadin.crudui.CrudLayout;
import org.vaadin.crudui.impl.layout.WindowBasedCrudLayout;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Alejandro Duarte
 */
public class GridBasedCrudComponent<T> extends AbstractCrudComponent<T> {

    private String findAllCaption = "Refresh";
    private String addCaption = "Add";
    private String updateCaption = "Update";
    private String deleteCaption = "Delete";
    private String deletedCaption = "Deleted";
    private String rowCountCaption = "%d row(s) found";
    private String saveCaption = "Save";
    private String savedCaption = "Saved";
    private String formErrorMessage = "Fix the errors and try again";
    private String okCaption = "Ok";

    private Button findAllButton;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Grid grid = new Grid();
    private BeanItemContainer<T> container;

    public GridBasedCrudComponent(Class<T> domainType) {
        this(domainType, new WindowBasedCrudLayout());
    }

    public GridBasedCrudComponent(Class<T> domainType, CrudLayout mainLayout) {
        super(domainType, mainLayout);

        findAllButton = new Button("", e -> refreshTableButtonClicked());
        findAllButton.setIcon(FontAwesome.REFRESH);
        setFindAllCaption(findAllCaption);
        mainLayout.addToolbarComponent(findAllButton);

        addButton = new Button("", e -> addButtonClicked());
        addButton.setIcon(FontAwesome.PLUS_CIRCLE);
        setAddCaption(addCaption);
        mainLayout.addToolbarComponent(addButton);

        updateButton = new Button("", e -> updateButtonClicked());
        updateButton.setIcon(FontAwesome.PENCIL);
        setUpdateCaption(updateCaption);
        mainLayout.addToolbarComponent(updateButton);

        deleteButton = new Button("", e -> deleteButtonClicked());
        deleteButton.setIcon(FontAwesome.TIMES);
        setDeleteCaption(deleteCaption);
        mainLayout.addToolbarComponent(deleteButton);

        updateButtons();

        grid.setSizeFull();
        grid.setContainerDataSource(container = new BeanItemContainer<>(domainType));
        grid.addSelectionListener(e -> gridSelectionChanged());
        mainLayout.setMainComponent(grid);
    }

    @Override
    public void setAddOptionVisible(boolean visible) {
        addButton.setVisible(visible);
    }

    @Override
    public void setUpdateOptionVisible(boolean visible) {
        updateButton.setVisible(visible);
    }

    @Override
    public void setDeleteOptionVisible(boolean visible) {
        deleteButton.setVisible(visible);
    }

    @Override
    public void setFindAllOptionVisible(boolean visible) {
        findAllButton.setVisible(false);
    }

    @Override
    public void setVisiblePropertyIds(Object... visiblePropertyIds) {
        super.setVisiblePropertyIds(visiblePropertyIds);
        grid.setColumns(visiblePropertyIds);
    }

    @Override
    public void setFindAllOperation(Supplier<Collection<T>> findAllOperation) {
        super.setFindAllOperation(findAllOperation);
        refreshGrid();
    }

    public void removeAll() {
        container.removeAllItems();
    }

    public void addAll(Collection<T> collection) {
        if (collection != null) {
            container.addAll(collection);
        }
    }

    public Grid getGrid() {
        return grid;
    }

    public BeanItemContainer getGridContainer() {
        return container;
    }

    public void refreshGrid() {
        removeAll();
        Collection all = findAllOperation.get();
        addAll(all);
    }

    private void refreshTableButtonClicked() {
        refreshGrid();
        Notification.show(String.format(rowCountCaption, container.size()));
    }

    private void updateButtons() {
        boolean enabled = grid.getSelectedRow() != null;
        updateButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
    }

    private void gridSelectionChanged() {
        updateButtons();
        T domainObject = (T) grid.getSelectedRow();

        if (domainObject != null) {
            Component crudForm = buildForm("", domainObject, updateFormVisiblePropertyIds, updateFormDisabledPropertyIds, updateFormFieldCaptions, true, okCaption, null, null, e -> {
                grid.select(null);
            });

            mainLayout.showReadForm("", crudForm);
        } else {
            mainLayout.hideForm();
        }
    }

    private void addButtonClicked() {
        try {
            T domainObject = domainType.newInstance();
            Component crudForm = buildForm(addCaption, domainObject, addFormVisiblePropertyIds, null, addFormFieldCaptions, false, saveCaption, FontAwesome.SAVE, ValoTheme.BUTTON_PRIMARY, e -> {
                addOperation.accept(domainObject);
                Notification.show(savedCaption);
            });

            mainLayout.showAddForm(addCaption, crudForm);

        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    private void updateButtonClicked() {
        T domainObject = (T) grid.getSelectedRow();

        Component crudForm = buildForm(updateCaption, domainObject, updateFormVisiblePropertyIds, updateFormDisabledPropertyIds, updateFormFieldCaptions, false, saveCaption, FontAwesome.SAVE, ValoTheme.BUTTON_PRIMARY, e -> {
            updateOperation.accept(domainObject);
            Notification.show(savedCaption);
        });

        mainLayout.showUpdateForm(updateCaption, crudForm);
    }

    private void deleteButtonClicked() {
        T domainObject = (T) grid.getSelectedRow();

        Component crudForm = buildForm(deleteCaption, domainObject, deleteFormVisiblePropertyIds, null, deleteFormFieldCaptions, true, deleteCaption, FontAwesome.TIMES, ValoTheme.BUTTON_DANGER, e -> {
            deleteOperation.accept(domainObject);
            grid.select(null);
            Notification.show(deletedCaption);
        });

        mainLayout.showDeleteForm(deleteCaption, crudForm);
    }

    private Component buildForm(String caption, T domainObject, List<Object> visiblePropertyIds, List<Object> disabledPropertyIds, List<String> fieldCaptions, boolean readOnly, String buttonCaption, Resource buttonIcon, String buttonStyle, ClickListener buttonClickListener) {
        List<CrudFieldConfiguration> fieldConfigurations = buildFieldConfigurations(domainObject, visiblePropertyIds, disabledPropertyIds, fieldCaptions, readOnly);
        CrudFormConfiguration formConfiguration = new CrudFormConfiguration(
                fieldConfigurations,
                buttonCaption,
                formErrorMessage,
                buttonIcon,
                buttonStyle,
                e -> {
                    buttonClickListener.buttonClick(e);
                    refreshGrid();
                    mainLayout.hideForm();
                    gridSelectionChanged();
                }
        );

        return getCrudFormFactory().buildNewForm(domainObject, formConfiguration);
    }

    public Button getFindAllButton() {
        return findAllButton;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setFindAllCaption(String findAllCaption) {
        this.findAllCaption = findAllCaption;
    }

    public void setAddCaption(String addCaption) {
        this.addCaption = addCaption;
        addButton.setDescription(addCaption);
    }

    public void setUpdateCaption(String updateCaption) {
        this.updateCaption = updateCaption;
        updateButton.setDescription(updateCaption);
    }

    public void setDeleteCaption(String deleteCaption) {
        this.deleteCaption = deleteCaption;
        deleteButton.setDescription(deleteCaption);
    }

    public void setDeletedCaption(String deletedCaption) {
        this.deletedCaption = deletedCaption;
    }

    public void setRowCountCaption(String rowCountCaption) {
        this.rowCountCaption = rowCountCaption;
    }

    public void setSaveCaption(String saveCaption) {
        this.saveCaption = saveCaption;
    }

    public void setSavedCaption(String savedCaption) {
        this.savedCaption = savedCaption;
    }

    public void setFormErrorMessage(String formErrorMessage) {
        this.formErrorMessage = formErrorMessage;
    }

    public void setOkCaption(String okCaption) {
        this.okCaption = okCaption;
    }

}
