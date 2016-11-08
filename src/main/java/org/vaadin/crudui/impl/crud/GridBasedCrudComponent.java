package org.vaadin.crudui.impl.crud;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.crudui.AbstractCrudComponent;
import org.vaadin.crudui.CrudLayout;
import org.vaadin.crudui.impl.layout.VerticalCrudLayout;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Alejandro Duarte
 */
public class GridBasedCrudComponent<T> extends AbstractCrudComponent<T> {

    private Button refreshGridButton;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Grid grid = new Grid();

    private String refreshCaption = "Refresh";
    private String addCaption = "Add";
    private String editCaption = "Edit";
    private String deleteCaption = "Delete";
    private String deletedCaption = "Deleted";
    private String rowCountCaption = "%d row(s) found";
    private String saveCaption = "Save";
    private String savedCaption = "Saved";
    private String selectRowCaption = "Select a row";

    protected Supplier<Collection<T>> findAll = () -> Collections.emptyList();

    public GridBasedCrudComponent(Class<T> domainType) {
        this(domainType, new VerticalCrudLayout());
    }

    public GridBasedCrudComponent(Class<T> domainType, CrudLayout mainLayout) {
        super(domainType, mainLayout);

        refreshGridButton = new Button("", this::refreshTableButtonClicked);
        refreshGridButton.setIcon(FontAwesome.REFRESH);
        setRefreshCaption(refreshCaption);
        mainLayout.addToolbarComponent(refreshGridButton);

        addButton = new Button("", this::addButtonClicked);
        addButton.setIcon(FontAwesome.PLUS_CIRCLE);
        setAddCaption(addCaption);
        mainLayout.addToolbarComponent(addButton);

        editButton = new Button("", this::editButtonClicked);
        editButton.setIcon(FontAwesome.PENCIL);
        setEditCaption(editCaption);
        mainLayout.addToolbarComponent(editButton);

        deleteButton = new Button("", this::deleteButtonClicked);
        deleteButton.setIcon(FontAwesome.TIMES);
        setDeleteCaption(deleteCaption);
        mainLayout.addToolbarComponent(deleteButton);

        grid.setSizeFull();
        grid.setContainerDataSource(new BeanItemContainer<>(domainType));
        mainLayout.setMainComponent(grid);
    }

    public void setRefreshTableOption(boolean visible) {
        refreshGridButton.setVisible(visible);
    }

    @Override
    public void setAddOptionVisible(boolean visible) {
        addButton.setVisible(visible);
    }

    @Override
    public void setEditOptionVisible(boolean visible) {
        editButton.setVisible(visible);
    }

    @Override
    public void setDeleteOptionVisible(boolean visible) {
        deleteButton.setVisible(visible);
    }

    public void removeAll() {
        grid.setContainerDataSource(new BeanItemContainer<>(domainType));
    }

    public void addAll(Collection<T> collection) {
        if (collection != null) {
            grid.setContainerDataSource(new BeanItemContainer<>(domainType, collection));
        }
    }

    public Grid getGrid() {
        return grid;
    }

    private void refreshTableButtonClicked(ClickEvent event) {
        refreshTable();
        Notification.show(String.format(rowCountCaption, grid.getContainerDataSource().size()));
    }

    public void refreshTable() {
        removeAll();
        Collection all = findAll.get();
        addAll(all);
    }

    private void addButtonClicked(ClickEvent event) {
        try {
            T domainObject = domainType.newInstance();
            showFormWindow(addCaption, domainObject, newFormVisiblePropertyIds, null, newFormFieldCaptions, false, saveCaption, FontAwesome.SAVE, ValoTheme.BUTTON_PRIMARY, e -> {
                add.accept(domainObject);
                Notification.show(savedCaption);
            });

        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    private void editButtonClicked(ClickEvent event) {
        T domainObject = (T) grid.getSelectedRow();

        if (domainObject != null) {
            showFormWindow(editCaption, domainObject, editFormVisiblePropertyIds, editFormDisabledPropertyIds, editFormFieldCaptions, false, saveCaption, FontAwesome.SAVE, ValoTheme.BUTTON_PRIMARY, e -> {
                update.accept(domainObject);
                Notification.show(savedCaption);
            });
        } else {
            Notification.show(selectRowCaption);
        }
    }

    private void deleteButtonClicked(ClickEvent event) {
        T domainObject = (T) grid.getSelectedRow();

        if (domainObject != null) {
            showFormWindow(deleteCaption, domainObject, deleteFormVisiblePropertyIds, null, deleteFormFieldCaptions, true, deleteCaption, FontAwesome.TIMES, ValoTheme.BUTTON_DANGER, e -> {
                delete.accept(domainObject);
                Notification.show(deletedCaption);
            });
        } else {
            Notification.show(selectRowCaption);
        }
    }

    private void showFormWindow(String windowTitle, T domainObject, Object[] visiblePropertyIds, Object disabledPropertyIds[], String[] fieldCaptions, boolean readOnly, String buttonCaption, Resource buttonIcon, String buttonStyle, ClickListener saveButtonClickListener) {
        Window window = new Window(windowTitle);
        window.setModal(true);
        UI.getCurrent().addWindow(window);

        VerticalLayout windowLayout = new VerticalLayout();
        windowLayout.setSizeUndefined();
        windowLayout.setMargin(true);
        window.setContent(windowLayout);

        Component crudForm = crudFormBuilder.buildNewForm(domainObject, visiblePropertyIds, disabledPropertyIds, fieldCaptions, readOnly, buttonCaption, buttonIcon, buttonStyle, e -> {
            saveButtonClickListener.buttonClick(e);
            refreshTable();
            window.close();
        });

        crudForm.setReadOnly(readOnly);
        windowLayout.addComponent(crudForm);
    }

    public void setOperations(Consumer<T> add, Consumer<T> update, Consumer<T> delete, Supplier<Collection<T>> findAll) {
        super.setOperations(add, update, delete);
        setFindAllOperation(findAll);
    }

    public void setFindAllOperation(Supplier<Collection<T>> findAll) {
        this.findAll = findAll;
        refreshTable();
    }

    public void setCrudListener(GridCrudListener<T> crudListener) {
        super.setCrudListener(crudListener);
        setFindAllOperation(() -> crudListener.findAll());
    }

    public Button getRefreshGridButton() {
        return refreshGridButton;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setRefreshCaption(String refreshCaption) {
        this.refreshCaption = refreshCaption;
    }

    public void setAddCaption(String addCaption) {
        this.addCaption = addCaption;
        addButton.setDescription(addCaption);
    }

    public void setEditCaption(String editCaption) {
        this.editCaption = editCaption;
        editButton.setDescription(editCaption);
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

    public void setSelectRowCaption(String selectRowCaption) {
        this.selectRowCaption = selectRowCaption;
    }
}
