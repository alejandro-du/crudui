package org.vaadin.crudui2.form.field.provider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

public class DynamicFieldProvider<V> implements FieldProvider {

	public static class UnsupportedFieldTypeException extends RuntimeException {
		public UnsupportedFieldTypeException(String message) {
			super(message);
		}
	}

	private Class<V> javaType;

	public DynamicFieldProvider(Class<V> javaType) {
		this.javaType = javaType;
	}

	@Override
	public AbstractField buildField() {
		if (Boolean.class.isAssignableFrom(javaType) || boolean.class == javaType) {
			return new Checkbox();
		}

		if (LocalDate.class.isAssignableFrom(javaType) || Date.class.isAssignableFrom(javaType)) {
			return new DatePicker();
		}

		if (LocalDateTime.class.isAssignableFrom(javaType)) {
			return new DateTimePicker();
		}

		if (Double.class.isAssignableFrom(javaType) || javaType == double.class) {
			return new NumberField();
		}

		if (Integer.class.isAssignableFrom(javaType) || javaType == int.class) {
			return new IntegerField();
		}

		if (BigDecimal.class.isAssignableFrom(javaType)) {
			return new BigDecimalField();
		}

		if (Enum.class.isAssignableFrom(javaType)) {
			V[] enumConstants = javaType.getEnumConstants();
			ComboBox<V> comboBox = new ComboBox<>();
			comboBox.setItems(Arrays.asList(enumConstants));
			return comboBox;
		}

		if (String.class.isAssignableFrom(javaType)
				|| Character.class.isAssignableFrom(javaType)
				|| Byte.class.isAssignableFrom(javaType)
				|| javaType.isPrimitive()) {

			return new TextField();
		}

		throw new UnsupportedFieldTypeException("Unable to create field for type: " + javaType);
	}

}
