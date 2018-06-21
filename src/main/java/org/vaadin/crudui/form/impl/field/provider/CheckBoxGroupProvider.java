package org.vaadin.crudui.form.impl.field.provider;


import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.function.ValueProvider;
import org.vaadin.crudui.form.FieldProvider;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public class CheckBoxGroupProvider<T> implements FieldProvider {

    private Grid<T> grid = new Grid<>();

    public CheckBoxGroupProvider(String label, Collection<T> items, ValueProvider<T, String> itemCaptionGenerator) {
        grid.setItems(items);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(itemCaptionGenerator::apply);
        grid.setHeightByRows(true);
        grid.getHeaderRows().clear();
    }

    @Override
    public HasValueAndElement buildField() {
        return grid.asMultiSelect();
    }

}