package org.vaadin.crudui.crud.impl;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.provider.Query;
import org.vaadin.crudui.crud.*;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;
import org.vaadin.crudui.layout.CrudLayout;
import org.vaadin.crudui.layout.impl.WindowBasedCrudLayout;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public class GridCrud<BEAN_TYPE> extends AbstractCrud<BEAN_TYPE> {

    protected String rowCountCaption = "%d items(s) found";
    protected String savedMessage = "Item saved";
    protected String deletedMessage = "Item deleted";
    protected boolean showNotifications = true;

    protected Button findAllButton;
    protected Button addButton;
    protected Button updateButton;
    protected Button deleteButton;
    protected Grid<BEAN_TYPE> grid;

    private boolean clickRowToUpdate;

    public GridCrud(Class<BEAN_TYPE> beanType) {
        this(beanType, new WindowBasedCrudLayout(), new DefaultCrudFormFactory<>(beanType), null);
    }

    public GridCrud(Class<BEAN_TYPE> beanType, CrudLayout crudLayout) {
        this(beanType, crudLayout, new DefaultCrudFormFactory<>(beanType), null);
    }

    public GridCrud(Class<BEAN_TYPE> beanType, CrudFormFactory<BEAN_TYPE> crudFormFactory) {
        this(beanType, new WindowBasedCrudLayout(), crudFormFactory, null);
    }

    public GridCrud(Class<BEAN_TYPE> beanType, CrudListener<BEAN_TYPE> crudListener) {
        this(beanType, new WindowBasedCrudLayout(), new DefaultCrudFormFactory<>(beanType), crudListener);
    }

    public GridCrud(Class<BEAN_TYPE> beanType, CrudLayout crudLayout, CrudFormFactory<BEAN_TYPE> crudFormFactory) {
        this(beanType, crudLayout, crudFormFactory, null);
    }

    public GridCrud(Class<BEAN_TYPE> beanType, CrudLayout crudLayout, CrudFormFactory<BEAN_TYPE> crudFormFactory,
            CrudListener<BEAN_TYPE> crudListener) {
        super(beanType, crudLayout, crudFormFactory, crudListener);
        initLayout();
    }

    protected void initLayout() {
        findAllButton = new Button(VaadinIcon.REFRESH.create(), e -> findAllButtonClicked());
        findAllButton.getElement().setAttribute("title", "Refresh list");

        crudLayout.addToolbarComponent(findAllButton);

        addButton = new Button(VaadinIcon.PLUS.create(), e -> addButtonClicked());
        addButton.getElement().setAttribute("title", "Add");
        crudLayout.addToolbarComponent(addButton);

        updateButton = new Button(VaadinIcon.PENCIL.create(), e -> updateButtonClicked());
        updateButton.getElement().setAttribute("title", "Update");
        crudLayout.addToolbarComponent(updateButton);

        deleteButton = new Button(VaadinIcon.TRASH.create(), e -> deleteButtonClicked());
        deleteButton.getElement().setAttribute("title", "Delete");
        crudLayout.addToolbarComponent(deleteButton);

        grid = new Grid<>(beanType);
        grid.setColumnReorderingAllowed(true);
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
        if (LazyFindAllCrudOperationListener.class.isAssignableFrom(findAllOperation.getClass())) {
            LazyFindAllCrudOperationListener findAll = (LazyFindAllCrudOperationListener) findAllOperation;

            grid.setDataProvider(findAll.getDataProvider());

        } else {
            Collection<BEAN_TYPE> items = findAllOperation.findAll();
            grid.setItems(items);
        }
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
        BEAN_TYPE bean = grid.asSingleSelect().getValue();

        if (bean != null) {
            if (clickRowToUpdate) {
                updateButtonClicked();
            } else {
                Component form = crudFormFactory.buildNewForm(CrudOperation.READ, bean, true, null, event -> {
                    grid.asSingleSelect().clear();
                });
                String caption = crudFormFactory.buildCaption(CrudOperation.READ, bean);
                crudLayout.showForm(CrudOperation.READ, form, caption);
            }
        } else {
            crudLayout.hideForm();
        }
    }

    protected void findAllButtonClicked() {
        grid.asSingleSelect().clear();
        refreshGrid();
        showNotification(String.format(rowCountCaption, grid.getDataProvider().size(new Query())));
    }

    protected void addButtonClicked() {
        try {
            BEAN_TYPE bean = beanType.newInstance();
            showForm(CrudOperation.ADD, bean, false, savedMessage, event -> {
                try {
                    BEAN_TYPE addedObject = addOperation.perform(bean);
                    refreshGrid();
                    grid.asSingleSelect().setValue(addedObject);
                    // TODO: grid.scrollTo(addedObject);
                } catch (IllegalArgumentException ignore) {
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
        BEAN_TYPE bean = grid.asSingleSelect().getValue();
        showForm(CrudOperation.UPDATE, bean, false, savedMessage, event -> {
            try {
                BEAN_TYPE updatedObject = updateOperation.perform(bean);
                grid.asSingleSelect().clear();
                refreshGrid();
                grid.asSingleSelect().setValue(updatedObject);
                // TODO: grid.scrollTo(updatedObject);
            } catch (IllegalArgumentException ignore) {
            } catch (CrudOperationException e1) {
                refreshGrid();
            } catch (Exception e2) {
                refreshGrid();
                throw e2;
            }
        });
    }

    protected void deleteButtonClicked() {
        BEAN_TYPE bean = grid.asSingleSelect().getValue();
        showForm(CrudOperation.DELETE, bean, true, deletedMessage, event -> {
            try {
                deleteOperation.perform(bean);
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

    protected void showForm(CrudOperation operation, BEAN_TYPE bean, boolean readOnly, String successMessage,
            ComponentEventListener<ClickEvent<Button>> buttonClickListener) {
        Component form = crudFormFactory.buildNewForm(operation, bean, readOnly, cancelClickEvent -> {
            if (clickRowToUpdate) {
                grid.asSingleSelect().clear();
            } else {
                BEAN_TYPE selected = grid.asSingleSelect().getValue();
                crudLayout.hideForm();
                grid.asSingleSelect().clear();
                grid.asSingleSelect().setValue(selected);
            }
        }, operationPerformedClickEvent -> {
            buttonClickListener.onComponentEvent(operationPerformedClickEvent);
            if (!clickRowToUpdate) {
                crudLayout.hideForm();
            }
            showNotification(successMessage);
        });
        String caption = crudFormFactory.buildCaption(operation, bean);
        crudLayout.showForm(operation, form, caption);
    }

    public Grid<BEAN_TYPE> getGrid() {
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

    public void setShowNotifications(boolean showNotifications) {
        this.showNotifications = showNotifications;
    }

    public void showNotification(String text) {
        if (showNotifications) {
            Notification.show(text);
        }
    }

}