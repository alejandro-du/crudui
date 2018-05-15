package org.vaadin.crudui.crud.impl;

import java.util.Collection;

import org.vaadin.crudui.crud.AbstractCrud;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.CrudOperationException;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.form.impl.form.factory.VerticalCrudFormFactory;
import org.vaadin.crudui.layout.CrudLayout;
import org.vaadin.crudui.layout.impl.WindowBasedCrudLayout;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcons;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.provider.Query;

/**
 * @author Alejandro Duarte
 */
public class GridCrud<T> extends AbstractCrud<T> {

    protected String rowCountCaption = "%d items(s) found";
    protected String savedMessage = "Item saved";
    protected String deletedMessage = "Item deleted";

    protected Button findAllButton;
    protected Button addButton;
    protected Button updateButton;
    protected Button deleteButton;
    protected Grid<T> grid;

    protected Collection<T> items;
    private boolean clickRowToUpdate;

    public GridCrud(Class<T> domainType) {
        this(domainType, new WindowBasedCrudLayout(), new VerticalCrudFormFactory<>(domainType), null);
    }

    public GridCrud(Class<T> domainType, CrudLayout crudLayout) {
        this(domainType, crudLayout, new VerticalCrudFormFactory<>(domainType), null);
    }

    public GridCrud(Class<T> domainType, CrudFormFactory<T> crudFormFactory) {
        this(domainType, new WindowBasedCrudLayout(), crudFormFactory, null);
    }

    public GridCrud(Class<T> domainType, CrudListener<T> crudListener) {
        this(domainType, new WindowBasedCrudLayout(), new VerticalCrudFormFactory<>(domainType), crudListener);
    }

    public GridCrud(Class<T> domainType, CrudLayout crudLayout, CrudFormFactory<T> crudFormFactory) {
        this(domainType, crudLayout, crudFormFactory, null);
    }

    public GridCrud(Class<T> domainType, CrudLayout crudLayout, CrudFormFactory<T> crudFormFactory, CrudListener<T> crudListener) {
        super(domainType, crudLayout, crudFormFactory, crudListener);
        initLayout();
    }

    protected void initLayout() {
        findAllButton = new Button(new Icon(VaadinIcons.REFRESH), e -> findAllButtonClicked());
        // FIXME set with css
        // findAllButton.setDescription("Refresh list");

        crudLayout.addToolbarComponent(findAllButton);

        addButton = new Button(new Icon(VaadinIcons.PLUS), e -> addButtonClicked());
        // FIXME do with css
        // addButton.setDescription("Add");
        crudLayout.addToolbarComponent(addButton);

        updateButton = new Button(new Icon(VaadinIcons.PENCIL), e -> updateButtonClicked());
        // FIXME do with css
        // updateButton.setDescription("Update");
        crudLayout.addToolbarComponent(updateButton);

        deleteButton = new Button(new Icon(VaadinIcons.TRASH), e -> deleteButtonClicked());
        // FIXME do with css
        // deleteButton.setDescription("Delete");
        crudLayout.addToolbarComponent(deleteButton);

        grid = new Grid<>(domainType);
        grid.setSizeFull();
        grid.addSelectionListener(e -> gridSelectionChanged());
        crudLayout.setMainComponent(grid);

        updateButtons();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        refreshGrid();
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

    public void refreshGrid() {
        items = findAllOperation.findAll();
        grid.setItems(items);
    }

    public void setClickRowToUpdate(boolean clickRowToUpdate) {
        this.clickRowToUpdate = clickRowToUpdate;
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
            if (clickRowToUpdate) {
                updateButtonClicked();
            } else {
                Component form = crudFormFactory.buildNewForm(CrudOperation.READ, domainObject, true, null, event -> {
                    grid.asSingleSelect().clear();
                });

                crudLayout.showForm(CrudOperation.READ, form);
            }
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
                try {
                    T addedObject = addOperation.perform(domainObject);
                    refreshGrid();
                    if (items.contains(addedObject)) {
                        grid.asSingleSelect().setValue(addedObject);
                        // TODO: grid.scrollTo(addedObject);
                    }
                } catch (CrudOperationException e1) {
                    refreshGrid();
                } catch (Exception e2) {
                    refreshGrid();
                    throw e2;
                }
            });
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void updateButtonClicked() {
        T domainObject = grid.asSingleSelect().getValue();
        showForm(CrudOperation.UPDATE, domainObject, false, savedMessage, event -> {
            try {
                T updatedObject = updateOperation.perform(domainObject);
                grid.asSingleSelect().clear();
                refreshGrid();
                if (items.contains(updatedObject)) {
                    grid.asSingleSelect().setValue(updatedObject);
                    // TODO: grid.scrollTo(updatedObject);
                }
            } catch (CrudOperationException e1) {
                refreshGrid();
            } catch (Exception e2) {
                refreshGrid();
                throw e2;
            }
        });
    }

    protected void deleteButtonClicked() {
        T domainObject = grid.asSingleSelect().getValue();
        showForm(CrudOperation.DELETE, domainObject, true, deletedMessage, event -> {
            try {
                deleteOperation.perform(domainObject);
                refreshGrid();
                grid.asSingleSelect().clear();
            } catch (CrudOperationException e1) {
                refreshGrid();
            } catch (Exception e2) {
                refreshGrid();
                throw e2;
            }
        });
    }

    protected void showForm(CrudOperation operation, T domainObject, boolean readOnly, String successMessage, ComponentEventListener<ClickEvent<Button>> buttonClickListener) {
        Component form = crudFormFactory.buildNewForm(operation, domainObject, readOnly, cancelClickEvent -> {
            if (clickRowToUpdate) {
                grid.asSingleSelect().clear();
            } else {
                T selected = grid.asSingleSelect().getValue();
                crudLayout.hideForm();
                grid.asSingleSelect().clear();
                grid.asSingleSelect().setValue(selected);
            }
        }, operationPerformedClickEvent -> {
            crudLayout.hideForm();
            buttonClickListener.onComponentEvent(operationPerformedClickEvent);
            Notification.show(successMessage);
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

}