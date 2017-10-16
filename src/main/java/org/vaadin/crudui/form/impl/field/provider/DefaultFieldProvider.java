package org.vaadin.crudui.form.impl.field.provider;

import com.vaadin.data.HasValue;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import org.vaadin.crudui.form.FieldProvider;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

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
        if (Boolean.class.isAssignableFrom(type) || boolean.class == type) {
            return new CheckBox();
        }

        if (LocalDate.class.isAssignableFrom(type) || Date.class.isAssignableFrom(type)) {
            return new DateField();
        }

        if (Enum.class.isAssignableFrom(type)) {
            Object[] values = type.getEnumConstants();
            ComboBox comboBox = new ComboBox();
            comboBox.setItems(values);
            return comboBox;
        }

        if (Collection.class.isAssignableFrom(type)) {
            CheckBoxGroup comboBox = new CheckBoxGroup();
            return comboBox;
        }

        if (String.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)
        		|| Number.class.isAssignableFrom(type) || type.isPrimitive()) {
            return new TextField();
        }

        ComboBox comboBox = new ComboBox();
        return comboBox;
    }

}
