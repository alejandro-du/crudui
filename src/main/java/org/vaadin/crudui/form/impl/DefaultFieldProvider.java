package org.vaadin.crudui.form.impl;

import com.vaadin.data.HasValue;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import org.vaadin.crudui.form.FieldProvider;

import java.time.LocalDate;

/**
 * @author Alejandro Duarte.
 */
public class DefaultFieldProvider implements FieldProvider {

    private Class<?> type;

    public DefaultFieldProvider(Class<?> type) {
        this.type = type;
    }

    @Override
    public HasValue buildField() {
        if (Boolean.class.isAssignableFrom(type)) {
            return new CheckBox();
        }

        if (LocalDate.class.isAssignableFrom(type)) {
            return new DateField();
        }

        if (Enum.class.isAssignableFrom(type)) {
            Object[] values = type.getDeclaringClass().getEnumConstants();
            ComboBox comboBox = new ComboBox();
            comboBox.setItems(values);
            return comboBox;
        }

        if (String.class.isAssignableFrom(type)) {
            return new TextField();
        }

        return new TextField();
    }

}
