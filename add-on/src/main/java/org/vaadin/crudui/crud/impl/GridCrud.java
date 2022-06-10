package org.vaadin.crudui.crud.impl;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.LazyFindAllCrudOperationListener;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.layout.CrudLayout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * @author Alejandro Duarte
 */
public class GridCrud<T> extends AbstractGridCrud<T> {

    private Column<T> componentColumn;

    private final Map<T, Button> map = new WeakHashMap<>();

    public GridCrud(Class<T> domainType) {
        super(domainType);
    }

    public GridCrud(Class<T> domainType, CrudLayout crudLayout) {
        super(domainType, crudLayout);
    }

    public GridCrud(Class<T> domainType, CrudFormFactory<T> crudFormFactory) {
        super(domainType, crudFormFactory);
    }

    public GridCrud(Class<T> domainType, CrudListener<T> crudListener) {
        super(domainType, crudListener);
    }

    public GridCrud(Class<T> domainType, CrudLayout crudLayout, CrudFormFactory<T> crudFormFactory) {
        super(domainType, crudLayout, crudFormFactory);
    }

    public GridCrud(Class<T> domainType, CrudLayout crudLayout, CrudFormFactory<T> crudFormFactory, CrudListener<T> crudListener) {
        super(domainType, crudLayout, crudFormFactory, crudListener);
    }

    @Override
    protected Grid<T> createGrid() {
        return new Grid<T>(domainType);
    }

    public void refreshGrid() {
        if (LazyFindAllCrudOperationListener.class.isAssignableFrom(findAllOperation.getClass())) {
            LazyFindAllCrudOperationListener findAll = (LazyFindAllCrudOperationListener) findAllOperation;

            grid.setDataProvider(findAll.getDataProvider());

        } else {
            Collection<T> items = findAllOperation.findAll();
            grid.setItems(items);
        }
    }

    public void addUpdateButtonColumn() {
        if (componentColumn == null) {
            componentColumn = grid.addComponentColumn(item -> {
                Button button = new Button(VaadinIcon.PENCIL.create());
                button.addClickListener(e -> {
                    grid.select(item);
                    updateButtonClicked();
                });

                if (map.put(item, button) == null) {
                    button.setEnabled(false);
                }

                return button;
            });
        }
    }

    public Button getUpdateButton(T item) {
        return map.get(item);
    }

    public void setUpdateButtonColumnEnabled(boolean enabled) {
        map.values().stream().forEach(b -> b.setEnabled(enabled));
    }
}