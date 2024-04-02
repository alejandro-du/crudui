package org.vaadin.crudui.form.impl.field.provider;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.crudui.form.FieldProvider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    public HasValueAndElement buildField(Object t) {
        if (Boolean.class.isAssignableFrom(type) || boolean.class == type) {
            return new Checkbox();
        }

        if (LocalDate.class.isAssignableFrom(type) || Date.class.isAssignableFrom(type)) {
            return new DatePicker();
        }

        if (LocalDateTime.class.isAssignableFrom(type)) {
            return new DateTimePicker();
        }

        if (Double.class.isAssignableFrom(type) || type == double.class) {
            return new NumberField();
        }

        if (Integer.class.isAssignableFrom(type) || type == int.class) {
            return new IntegerField();
        }

        if (BigDecimal.class.isAssignableFrom(type)) {
            return new BigDecimalField();
        }

        if (Enum.class.isAssignableFrom(type)) {
            Object[] values = type.getEnumConstants();
            ComboBox comboBox = new ComboBox<>();
            comboBox.setItems(Arrays.asList(values));
            return comboBox;
        }

        if (String.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)
                || Number.class.isAssignableFrom(type) || type.isPrimitive()) {
            return new TextField();
        }

        return null;
    }

}
