package org.vaadin.crudui.crud.impl;

import com.vaadin.data.provider.Query;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import org.vaadin.crudui.crud.AbstractCrudComponent;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.CrudOperationException;
import org.vaadin.crudui.crud.FindAllCrudOperationListener;
import org.vaadin.crudui.layout.CrudLayout;
import org.vaadin.crudui.layout.impl.WindowBasedCrudLayout;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author Alejandro Duarte
 */
public class GridBasedCrudComponent<T> extends AbstractCrudComponent<T> {

    private String rowCountCaption = "%d items(s) found";
    private String savedMessage = "Item saved";
    private String deletedMessage = "Item deleted";
    private Consumer<Exception> errorConsumer;

    private Button findAllButton;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Grid<T> grid;

    Collection<T> items;

    public GridBasedCrudComponent(Class<T> domainType) {
        this(domainType, new WindowBasedCrudLayout());
    }

    public GridBasedCrudComponent(Class<T> domainType, CrudListener<T> crudListener) {
        this(domainType, new WindowBasedCrudLayout());
        setCrudListener(crudListener);
    }

    public GridBasedCrudComponent(Class<T> domainType, CrudLayout crudLayout) {
        super(domainType, crudLayout);
        initLayout();
    }

    protected void initLayout() {
        findAllButton = new Button("", e -> findAllButtonClicked());
        findAllButton.setDescription("Refresh list");
        findAllButton.setIcon(VaadinIcons.REFRESH);
        crudLayout.addToolbarComponent(findAllButton);

        addButton = new Button("", e -> addButtonClicked());
        addButton.setDescription("Add");
        addButton.setIcon(VaadinIcons.PLUS);
        crudLayout.addToolbarComponent(addButton);

        updateButton = new Button("", e -> updateButtonClicked());
        updateButton.setDescription("Update");
        updateButton.setIcon(VaadinIcons.PENCIL);
        crudLayout.addToolbarComponent(updateButton);

        deleteButton = new Button("", e -> deleteButtonClicked());
        deleteButton.setDescription("Delete");
        deleteButton.setIcon(VaadinIcons.TRASH);
        crudLayout.addToolbarComponent(deleteButton);

        grid = new Grid<>(domainType);
        grid.setSizeFull();
        grid.addSelectionListener(e -> gridSelectionChanged());
        crudLayout.setMainComponent(grid);

        updateButtons();
    }

    @Override
    public void setAddOperationVisible(boolean visible) {
        addButton.setVisible(visible);
    }

    @Override
    public void setUpdateOperationVisible(boolean visible) {
        updateButton.setVisible(visible);
    }

    @Override
    public void setDeleteOperationVisible(boolean visible) {
        deleteButton.setVisible(visible);
    }

    @Override
    public void setFindAllOperationVisible(boolean visible) {
        findAllButton.setVisible(false);
    }

    @Override
    public void setFindAllOperation(FindAllCrudOperationListener<T> findAllOperation) {
        super.setFindAllOperation(findAllOperation);
        refreshGrid();
    }

    public void refreshGrid() {
        items = findAllOperation.findAll();
        grid.setItems(items);
    }

    protected void updateButtons() {
        boolean rowSelected = !grid.asSingleSelect().isEmpty();
        updateButton.setEnabled(rowSelected);
        deleteButton.setEnabled(rowSelected);
    }

    protected void gridSelectionChanged() {
        updateButtons();
        T domainObject = grid.asSingleSelect().getValue();

        if (domainObject != null) {
            Component form = crudFormFactory.buildNewForm(CrudOperation.READ, domainObject, true, null, event -> {
                grid.asSingleSelect().clear();
            });

            crudLayout.showForm(CrudOperation.READ, form);
        } else {
            crudLayout.hideForm();
        }
    }

    protected void findAllButtonClicked() {
        grid.asSingleSelect().clear();
        refreshGrid();
        Notification.show(String.format(rowCountCaption, grid.getDataProvider().size(new Query())));
    }

    protected void addButtonClicked() {
        try {
            T domainObject = domainType.newInstance();
            showForm(CrudOperation.ADD, domainObject, false, savedMessage, event -> {
                T addedObject = addOperation.perform(domainObject);
                refreshGrid();
                if (items.contains(addedObject)) {
                    grid.asSingleSelect().setValue(addedObject);
                    // TODO: grid.scrollTo(addedObject);
                }
            });
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void updateButtonClicked() {
        T domainObject = grid.asSingleSelect().getValue();
        showForm(CrudOperation.UPDATE, domainObject, false, savedMessage, event -> {
            T updatedObject = updateOperation.perform(domainObject);
            grid.asSingleSelect().clear();
            refreshGrid();
            if (items.contains(updatedObject)) {
                grid.asSingleSelect().setValue(updatedObject);
                // TODO: grid.scrollTo(updatedObject);
            }
        });
    }

    protected void deleteButtonClicked() {
        T domainObject = grid.asSingleSelect().getValue();
        showForm(CrudOperation.DELETE, domainObject, true, deletedMessage, event -> {
            deleteOperation.perform(domainObject);
            refreshGrid();
            grid.asSingleSelect().clear();
        });
    }

    protected void showForm(CrudOperation operation, T domainObject, boolean readOnly, String successMessage, Button.ClickListener buttonClickListener) {
        Component form = crudFormFactory.buildNewForm(operation, domainObject, readOnly,
                event -> {
                    T selected = grid.asSingleSelect().getValue();
                    crudLayout.hideForm();
                    grid.asSingleSelect().clear();
                    grid.asSingleSelect().setValue(selected);
                },
                event -> {
                    try {
                        crudLayout.hideForm();
                        buttonClickListener.buttonClick(event);
                        Notification.show(successMessage);

                    } catch (CrudOperationException e) {
                        Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    } catch (Exception e) {
                        if (errorConsumer != null) {
                            errorConsumer.accept(e);
                        } else {
                            Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
                            throw new RuntimeException("Error executing " + operation.name() + " operation", e);
                        }
                    }
                });

        crudLayout.showForm(operation, form);
    }

    public Grid<T> getGrid() {
        return grid;
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

    public void setRowCountCaption(String rowCountCaption) {
        this.rowCountCaption = rowCountCaption;
    }

    public void setSavedMessage(String savedMessage) {
        this.savedMessage = savedMessage;
    }

    public void setDeletedMessage(String deletedMessage) {
        this.deletedMessage = deletedMessage;
    }

    public void setErrorConsumer(Consumer<Exception> errorConsumer) {
        this.errorConsumer = errorConsumer;
    }

}