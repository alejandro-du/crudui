package org.vaadin.crudui.crud.impl;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import org.vaadin.crudui.CrudOperation;
import org.vaadin.crudui.crud.AbstractCrudComponent;
import org.vaadin.crudui.layout.CrudLayout;
import org.vaadin.crudui.layout.impl.WindowBasedCrudLayout;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Alejandro Duarte
 */
public class GridBasedCrudComponent<T> extends AbstractCrudComponent<T> {

    private String rowCountCaption = "%d items(s) found";
    private String okCaption = "Ok";
    private String savedMessage = "Item saved";
    private String deletedMessage = "Item deleted";

    private Button findAllButton;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Grid grid = new Grid();
    private BeanItemContainer<T> container;

    public GridBasedCrudComponent(Class<T> domainType) {
        this(domainType, new WindowBasedCrudLayout());
    }

    public GridBasedCrudComponent(Class<T> domainType, CrudLayout crudLayout) {
        super(domainType, crudLayout);
        initLayout();
    }

    protected void initLayout() {
        findAllButton = new Button("", e -> findAllButtonClicked());
        findAllButton.setIcon(FontAwesome.REFRESH);
        crudLayout.addToolbarComponent(findAllButton);

        addButton = new Button("", e -> addButtonClicked());
        addButton.setIcon(FontAwesome.PLUS_CIRCLE);
        crudLayout.addToolbarComponent(addButton);

        updateButton = new Button("", e -> updateButtonClicked());
        updateButton.setIcon(FontAwesome.PENCIL);
        crudLayout.addToolbarComponent(updateButton);

        deleteButton = new Button("", e -> deleteButtonClicked());
        deleteButton.setIcon(FontAwesome.TIMES);
        crudLayout.addToolbarComponent(deleteButton);

        grid.setSizeFull();
        grid.setContainerDataSource(container = new BeanItemContainer<>(domainType));
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
    public void setFindAllOperation(Supplier<Collection<T>> findAllOperation) {
        super.setFindAllOperation(findAllOperation);
        refreshGrid();
    }

    public void refreshGrid() {
        container.removeAllItems();
        Collection all = findAllOperation.get();
        container.addAll(all);
    }

    protected void updateButtons() {
        boolean enabled = grid.getSelectedRow() != null;
        updateButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
    }

    protected void gridSelectionChanged() {
        updateButtons();
        T domainObject = (T) grid.getSelectedRow();

        if (domainObject != null) {
            Component form = crudFormFactory.buildNewForm(CrudOperation.READ, domainObject, true, object -> {
                grid.select(null);
            });

            crudLayout.showForm(CrudOperation.READ, form);
        } else {
            crudLayout.hideForm();
        }
    }

    protected void findAllButtonClicked() {
        grid.select(null);
        refreshGrid();
        Notification.show(String.format(rowCountCaption, container.size()));
    }

    protected void addButtonClicked() {
        try {
            T domainObject = domainType.newInstance();
            Component form = crudFormFactory.buildNewForm(CrudOperation.ADD, domainObject, false, object -> {
                addOperation.accept(domainObject);
                crudLayout.hideForm();
                refreshGrid();
                grid.select(null);
                grid.select(domainObject);
                Notification.show(savedMessage);
            });

            crudLayout.showForm(CrudOperation.ADD, form);

        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    protected void updateButtonClicked() {
        T domainObject = (T) grid.getSelectedRow();

        Component form = crudFormFactory.buildNewForm(CrudOperation.UPDATE, domainObject, false, object -> {
            updateOperation.accept(domainObject);
            crudLayout.hideForm();
            refreshGrid();
            grid.select(null);
            grid.select(domainObject);
            Notification.show(savedMessage);
        });

        crudLayout.showForm(CrudOperation.UPDATE, form);
    }

    protected void deleteButtonClicked() {
        T domainObject = (T) grid.getSelectedRow();

        Component form = crudFormFactory.buildNewForm(CrudOperation.DELETE, domainObject, true, object -> {
            deleteOperation.accept(domainObject);
            crudLayout.hideForm();
            refreshGrid();
            grid.select(null);
            Notification.show(deletedMessage);
        });

        crudLayout.showForm(CrudOperation.DELETE, form);
    }

    public Grid getGrid() {
        return grid;
    }

    public BeanItemContainer getGridContainer() {
        return container;
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

    public void setOkCaption(String okCaption) {
        this.okCaption = okCaption;
    }

    public void setSavedMessage(String savedMessage) {
        this.savedMessage = savedMessage;
    }

    public void setDeletedMessage(String deletedMessage) {
        this.deletedMessage = deletedMessage;
    }

}
