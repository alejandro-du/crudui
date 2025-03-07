package org.vaadin.crudui2.form.provider;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

public class TypeBasedFieldProvider<B, V> implements FieldProvider<B, AbstractField<?, V>> {

	public static class UnsupportedFieldTypeException extends RuntimeException {
		public UnsupportedFieldTypeException(String message) {
			super(message);
		}

		public UnsupportedFieldTypeException(String message, Exception cause) {
			super(message, cause);
		}
	}

	private Class<? extends AbstractField> fieldType;
	private Class<V> fieldValueType;
	private String propertyName;

	public TypeBasedFieldProvider(Class<? extends AbstractField> fieldType, Class<V> fieldValueType, String propertyName) {
		this.fieldType = fieldType;
		this.fieldValueType = fieldValueType;
		this.propertyName = propertyName;
	}

	@Override
	public AbstractField buildField(B bean) {
		if (fieldType != null) {
			return buildByFieldType(fieldType);

		} else if (fieldValueType != null) {
			return buildByFieldValueType(fieldValueType);

		} else {
			return buildByBeanType(bean.getClass());
		}
	}

	private static AbstractField buildByFieldType(Class<? extends AbstractField> fieldType) {
		try {
			return fieldType.getDeclaredConstructor().newInstance();

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new UnsupportedFieldTypeException("Unable to instantiate field type: " + fieldType, e);
		}
	}

	private static AbstractField buildByFieldValueType(Class<?> fieldValueType) {
		AbstractField field;

		if (Boolean.class.isAssignableFrom(fieldValueType) || boolean.class == fieldValueType) {
			field = new Checkbox();
		} else if (LocalDate.class.isAssignableFrom(fieldValueType) || Date.class.isAssignableFrom(fieldValueType)) {
			field = new DatePicker();

		} else if (LocalDateTime.class.isAssignableFrom(fieldValueType)) {
			field = new DateTimePicker();

		} else if (Double.class.isAssignableFrom(fieldValueType) || fieldValueType == double.class) {
			field = new NumberField();

		} else if (Integer.class.isAssignableFrom(fieldValueType) || fieldValueType == int.class) {
			field = new IntegerField();

		} else if (BigDecimal.class.isAssignableFrom(fieldValueType)) {
			field = new BigDecimalField();

		} else if (Enum.class.isAssignableFrom(fieldValueType)) {
			field = new RadioButtonGroup<>();

		} else if (String.class.isAssignableFrom(fieldValueType)
				|| Character.class.isAssignableFrom(fieldValueType)
				|| Byte.class.isAssignableFrom(fieldValueType)
				|| fieldValueType.isPrimitive()) {

			field = new TextField();

		} else {
			throw new UnsupportedFieldTypeException("Unable to create field for java type: " + fieldValueType);
		}

		return field;
	}

	private AbstractField buildByBeanType(Class<?> beanType) {
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructType(beanType);
		var beanDescription = (BasicBeanDescription) mapper.getSerializationConfig().introspect(javaType);
		BeanPropertyDefinition property = beanDescription.findProperty(new PropertyName(propertyName));
		Class<?> propertyType = property.getRawPrimaryType();
		return buildByFieldValueType(propertyType);
	}

}
