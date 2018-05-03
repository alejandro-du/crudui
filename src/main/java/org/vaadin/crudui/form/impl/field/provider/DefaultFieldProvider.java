package org.vaadin.crudui.form.impl.field.provider;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.vaadin.crudui.form.FieldProvider;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.selection.MultiSelect;

/**
 * @author Alejandro Duarte.
 */
public class DefaultFieldProvider<T> implements FieldProvider {

    private Class<?> type;

    public DefaultFieldProvider(Class<?> type) {
        this.type = type;
    }

    @Override
    public HasValue buildField() {
        if (Boolean.class.isAssignableFrom(type) || boolean.class == type) {
            return new Checkbox();
        }

        if (LocalDate.class.isAssignableFrom(type) || Date.class.isAssignableFrom(type)) {
            return new DatePicker();
        }

        if (Enum.class.isAssignableFrom(type)) {
            Object[] values = type.getEnumConstants();
            ComboBox comboBox = new ComboBox<>();
            comboBox.setItems(Arrays.asList(values));
            return comboBox;
        }

        // FIXME missing CheckBoxGroup
        if (Collection.class.isAssignableFrom(type)) {
            Grid<?> grid = new Grid<>();
            grid.setSelectionMode(SelectionMode.MULTI);
            grid.addColumn(new TextRenderer<>(item -> item.toString()));
            return grid.asMultiSelect();
        }

        if (String.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)
                || Number.class.isAssignableFrom(type) || type.isPrimitive()) {
            return new TextField();
        }

        ComboBox comboBox = new ComboBox();
        return comboBox;
    }

}
