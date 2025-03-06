package org.vaadin.crudui2.form.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.function.ValueProvider;

import org.vaadin.crudui.form.FieldCreationListener;
import org.vaadin.crudui2.form.field.provider.FieldProvider;

public class CrudField<B, V, C extends AbstractField<C, V>> {

	public static interface UpdateHandler<B> {
		public void onUpdate(AbstractField fieldToUpdate, B bean);
	}

	public static class Builder<B, V, C extends AbstractField<C, V>> {

		private ValueProvider<B, V> getter;
		private Setter<B, V> setter;
		private Class<V> fieldValueType;
		private String propertyName;
		private String label;
		private boolean enabled = true;
		private FieldCreationListener fieldCreationListener;
		private FieldProvider<?, ?> fieldProvider;
		private Class<AbstractField<C, V>> fieldType;
		private Converter<?, V> converter;
		private List<Builder<B, ?, ?>> valueChangeListeners = new ArrayList<>();
		private UpdateHandler<B> updateHandler;

		public Builder(ValueProvider<B, V> getter, Setter<B, V> setter, Class<V> fieldValueType) {
			this.getter = Objects.requireNonNull(getter, "Getter cannot be null");
			this.setter = Objects.requireNonNull(setter, "Setter cannot be null");
			this.fieldValueType = Objects.requireNonNull(fieldValueType, "Field value type cannot be null");
		}

		public Builder(String propertyName, Class<V> fieldValueType) {
			this.propertyName = Objects.requireNonNull(propertyName, "Property name cannot be null");
			this.fieldValueType = Objects.requireNonNull(fieldValueType, "Field value type cannot be null");
			// TODO: can we infer fieldValueType from propertyName?
		}

		public Builder<B, V, C> label(String label) {
			this.label = label;
			return this;
		}

		public Builder<B, V, C> enabled(boolean enabled) {
			this.enabled = enabled;
			return this;
		}

		public Builder<B, V, C> fieldCreationListener(FieldCreationListener fieldCreationListener) {
			this.fieldCreationListener = fieldCreationListener;
			return this;
		}

		public Builder<B, V, C> fieldProvider(FieldProvider<?, ?> fieldProvider) {
			this.fieldProvider = fieldProvider;
			return this;
		}

		public Builder<B, V, C> fieldType(Class<AbstractField<C, V>> fieldType) {
			this.fieldType = fieldType;
			return this;
		}

		public Builder<B, V, C> converter(Converter<?, V> converter) {
			this.converter = converter;
			return this;
		}

		// TODO: rename to addValueChangeListener
		public Builder<B, V, C> onValueChangeUpdate(Builder<B, ?, ?> newValueChangeListener) {
			valueChangeListeners.add(newValueChangeListener);
			return this;
		}

		public Builder<B, V, C> onUpdate(UpdateHandler<B> updateHandler) {
			this.updateHandler = updateHandler;
			return this;
		}

		public CrudField<B, V, C> build() {
			return new CrudField<>(this);
		}
	}

	public static <B, V, C extends AbstractField<C, V>> Builder<B, V, C> of(
			ValueProvider<B, V> getter, Setter<B, V> setter, Class<V> fieldValueType) {
		return new Builder<>(getter, setter, fieldValueType);
	}

	public static <B, V, C extends AbstractField<C, V>> Builder<B, V, C> of(
			String propertyName, Class<V> fieldValueType) {
		return new Builder<>(propertyName, fieldValueType);
	}

	public static <B, V, T extends Collection<V>, C extends AbstractField<C, T>> Builder<B, T, C> ofCollection(
			ValueProvider<B, T> getter, Setter<B, T> setter, Class<V> fieldValueType) {
		Class<T> collectionType = (Class<T>) (Class<?>) Collection.class;
		return new Builder<>(getter, setter, collectionType);
	}

	public static <B, V, T extends Collection<V>, C extends AbstractField<C, T>> Builder<B, T, C> ofCollection(
			String propertyName, Class<V> fieldValueType) {
		Class<T> collectionType = (Class<T>) (Class<?>) Collection.class;
		return new Builder<>(propertyName, collectionType);
	}

	private final ValueProvider<B, V> getter;
	private final Setter<B, V> setter;
	private final Class<V> fieldValueType;
	private final String propertyName;
	private final String label;
	private final boolean enabled;
	private final FieldCreationListener fieldCreationListener;
	private final FieldProvider<?, ?> fieldProvider;
	private final Class<AbstractField<C, V>> fieldType;
	private final Converter<?, V> converter;
	private final List<Builder<B, ?, ?>> valueChangeListeners;
	private final UpdateHandler<B> updateHandler;

	private CrudField(Builder<B, V, C> builder) {
		this.getter = builder.getter;
		this.setter = builder.setter;
		this.fieldValueType = builder.fieldValueType;
		this.propertyName = builder.propertyName;
		this.label = builder.label;
		this.enabled = builder.enabled;
		this.fieldCreationListener = builder.fieldCreationListener;
		this.fieldProvider = builder.fieldProvider;
		this.fieldType = builder.fieldType;
		this.converter = builder.converter;
		this.valueChangeListeners = builder.valueChangeListeners;
		this.updateHandler = builder.updateHandler;
	}

	public ValueProvider<B, V> getGetter() {
		return getter;
	}

	public Setter<B, V> getSetter() {
		return setter;
	}

	public Class<V> getFieldValueType() {
		return fieldValueType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getLabel() {
		return label;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public FieldCreationListener getFieldCreationListener() {
		return fieldCreationListener;
	}

	public FieldProvider<?, ?> getFieldProvider() {
		return fieldProvider;
	}

	public Class<AbstractField<C, V>> getFieldType() {
		return fieldType;
	}

	public Converter<?, V> getConverter() {
		return converter;
	}

	public List<Builder<B, ?, ?>> getValueChangeListeners() {
		return valueChangeListeners;
	}

	public UpdateHandler<B> getUpdateHandler() {
		return updateHandler;
	}

}
