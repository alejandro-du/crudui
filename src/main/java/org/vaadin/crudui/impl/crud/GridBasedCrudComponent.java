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
import org.vaadin.crudui.CrudListener;
import org.vaadin.crudui.impl.layout.VerticalCrudLayout;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public class GridBasedCrudComponent<T> extends AbstractCrudComponent<T> {

    public static interface GridCrudListener<T> extends CrudListener<T> {
        Collection<T> findAll();
    }

    private Button refreshGridButton;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Grid grid = new Grid();

    public GridBasedCrudComponent(Class<T> domainType, GridCrudListener<T> crudListener) {
        this(domainType, crudListener, new VerticalCrudLayout());
    }

    public GridBasedCrudComponent(Class<T> domainType, GridCrudListener<T> crudListener, CrudLayout mainLayout) {
        super(domainType, crudListener, mainLayout);

        refreshGridButton = new Button("", this::refreshTableButtonClicked);
        refreshGridButton.setDescription("Refresh");
        refreshGridButton.setIcon(FontAwesome.REFRESH);

        addButton = new Button("", this::addButtonClicked);
        addButton.setDescription("Add");
        addButton.setIcon(FontAwesome.PLUS_CIRCLE);
        addButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        editButton = new Button("", this::editButtonClicked);
        editButton.setDescription("Edit");
        editButton.setIcon(FontAwesome.PENCIL);
        editButton.addStyleName(ValoTheme.BUTTON_PRIMARY);

        deleteButton = new Button("", this::deleteButtonClicked);
        deleteButton.setDescription("Delete");
        deleteButton.setIcon(FontAwesome.TIMES);
        deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);

        grid.setSizeFull();
        grid.setContainerDataSource(new BeanItemContainer<>(domainType));
        mainLayout.setMainComponent(grid);
        refreshTable();
    }

    @Override
    public void showAllOptions() {
        showRefreshTableOption();
        super.showAllOptions();
    }

    public void showRefreshTableOption() {
        mainLayout.addToolbarComponent(refreshGridButton);
    }

    @Override
    public void showAddOption() {
        mainLayout.addToolbarComponent(addButton);
    }

    @Override
    public void showEditOption() {
        mainLayout.addToolbarComponent(editButton);
    }

    @Override
    public void showDeleteOption() {
        mainLayout.addToolbarComponent(deleteButton);
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

    public GridCrudListener<T> getCrudListener() {
        return (GridCrudListener<T>) crudListener;
    }

    private void refreshTableButtonClicked(ClickEvent event) {
        refreshTable();
        Notification.show(grid.getContainerDataSource().size() + " Row(s)");
    }

    public void refreshTable() {
        removeAll();
        Collection all = ((GridCrudListener) crudListener).findAll();
        addAll(all);
    }

    private void addButtonClicked(ClickEvent event) {
        try {
            T domainObject = domainType.newInstance();
            showFormWindow("Add", domainObject, newFormVisiblePropertyIds, null, newFormFieldCaptions, false, "Save", FontAwesome.SAVE, ValoTheme.BUTTON_PRIMARY, e -> {
                crudListener.add(domainObject);
                Notification.show("Saved");
            });

        } catch (InstantiationException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
    }

    private void editButtonClicked(ClickEvent event) {
        T domainObject = (T) grid.getSelectedRow();

        if (domainObject != null) {
            showFormWindow("Edit", domainObject, editFormVisiblePropertyIds, editFormDisabledPropertyIds, editFormFieldCaptions, false, "Guardar", FontAwesome.SAVE, ValoTheme.BUTTON_PRIMARY, e -> {
                crudListener.update(domainObject);
                Notification.show("Saved");
            });
        } else {
            Notification.show("Select a row");
        }
    }

    private void deleteButtonClicked(ClickEvent event) {
        T domainObject = (T) grid.getSelectedRow();

        if (domainObject != null) {
            showFormWindow("Delete", domainObject, deleteFormVisiblePropertyIds, null, deleteFormFieldCaptions, true, "Delete", FontAwesome.TIMES, ValoTheme.BUTTON_DANGER, e -> {
                crudListener.delete(domainObject);
                Notification.show("Deleted");
            });
        } else {
            Notification.show("Select a row");
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

}
