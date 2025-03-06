package org.vaadin.crudui2.form;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.shared.HasClearButton;
import com.vaadin.flow.shared.util.SharedUtil;

import org.vaadin.crudui2.form.field.CrudField;
import org.vaadin.crudui2.form.field.CrudField.Builder;
import org.vaadin.crudui2.form.field.CrudField.UpdateHandler;
import org.vaadin.crudui2.form.field.provider.DynamicFieldProvider;
import org.vaadin.crudui2.form.field.provider.FieldProvider;

public class CrudFormFactory<B> {

	private Class<B> domainType;
	private List<CrudField.Builder<?, ?, ?>> fieldBuilders = new ArrayList<>();
	private boolean useBeanValidation;

	public CrudFormFactory(Class<B> domainType) {
		this.domainType = domainType;
		autoGenerateFieldBuilders();
	}

	public <V, C extends AbstractField<C, V>> CrudFormFactory<B> add(
			CrudField.Builder<B, ?, ?> fieldBuilder) {
		fieldBuilders.add(fieldBuilder);
		return this;
	}

	public CrudFormFactory<B> setFields(CrudField.Builder<?, ?, ?>... newFieldBuilders) {
		fieldBuilders.clear();
		fieldBuilders.addAll(List.of(newFieldBuilders));
		return this;
	}

	public CrudFormFactory<B> useBeanValidation() {
		useBeanValidation = true;
		return this;
	}

	public CrudForm<B> build() {
		CrudForm<B> form = new CrudForm<>(domainType, useBeanValidation);
		var vaadinFieldByBuilder = new HashMap<Builder<?, ?, ?>, AbstractField<?, ?>>();
		var crudFieldByVaadinField = new HashMap<AbstractField<?, ?>, CrudField<B, ?, ?>>();
		var vaadinFieldByCrudField = new HashMap<CrudField<B, ?, ?>, AbstractField<?, ?>>();
		var notifiers = new HashMap<AbstractField<?, ?>, List<Builder<B, ?, ?>>>();

		fieldBuilders.forEach(builder -> {
			try {
				CrudField crudField = builder.build();
				AbstractField vaadinField = buildField(crudField);
				configureVaadinField(vaadinField, crudField);

				if (crudField.getGetter() != null) {
					form.add(vaadinField, crudField.getGetter(), crudField.getSetter());
				} else {
					form.add(vaadinField, crudField.getPropertyName());
				}

				if (!crudField.getValueChangeListeners().isEmpty()) {
					notifiers.put(vaadinField, crudField.getValueChangeListeners());
				}

				vaadinFieldByBuilder.put(builder, vaadinField);
				crudFieldByVaadinField.put(vaadinField, crudField);
				vaadinFieldByCrudField.put(crudField, vaadinField);
			} catch (DynamicFieldProvider.UnsupportedFieldTypeException ignored) {
				// no field is created
			}
		});

		notifiers.forEach((vaadinFieldNotifier, builderListeners) -> {
			vaadinFieldNotifier.addValueChangeListener(event -> {
				builderListeners.forEach(builderListener -> {
					B bean = form.getValue();
					AbstractField<?, ?> vaadinField = vaadinFieldByBuilder.get(builderListener);
					CrudField crudField = crudFieldByVaadinField.get(vaadinField);
					UpdateHandler<B> updateHandler = crudField.getUpdateHandler();
					if (updateHandler != null) {
						updateHandler.onUpdate((AbstractField) vaadinField, bean);
					}
				});
			});
		});

		form.addValueChangeListener(event -> {
			crudFieldByVaadinField.values().forEach(crudField -> {
				if (crudField.getUpdateHandler() != null) {
					AbstractField<?, ?> vaadinField = vaadinFieldByCrudField.get(crudField);
					B bean = event.getValue();
					crudField.getUpdateHandler().onUpdate((AbstractField) vaadinField, bean);
				}
			});
		});

		return form;
	}

	private void autoGenerateFieldBuilders() {
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructType(domainType);
		var beanDescription = (BasicBeanDescription) mapper.getSerializationConfig().introspect(javaType);
		beanDescription.findProperties().forEach(property -> {
			String propertyName = property.getName();
			Class propertyType = property.getRawPrimaryType();
			var fieldBuilder = CrudField.of(propertyName, propertyType);
			fieldBuilder.label(SharedUtil.propertyIdToHumanFriendly(propertyName));
			fieldBuilders.add(fieldBuilder);
		});
	}

	private AbstractField<?, ?> buildField(CrudField<B, ?, ?> crudField) {
		FieldProvider fieldProvider = crudField.getFieldProvider();

		if (fieldProvider == null) {
			Class<?> fieldType = crudField.getFieldType();

			if (fieldType != null) {
				fieldProvider = () -> {
					try {
						return (AbstractField<?, ?>) fieldType.getDeclaredConstructor().newInstance();
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
						throw new IllegalStateException("Unable to instantiate field type: " + fieldType, e);
					}
				};
			} else {
				fieldProvider = (FieldProvider) new DynamicFieldProvider<>(crudField.getFieldValueType());
			}
		}

		return fieldProvider.buildField();
	}

	private void configureVaadinField(AbstractField<?, ?> vaadinField, CrudField<B, ?, ?> crudField) {
		if (HasLabel.class.isAssignableFrom(vaadinField.getClass())) {
			((HasLabel) vaadinField).setLabel(crudField.getLabel());
		}

		if (HasClearButton.class.isAssignableFrom(vaadinField.getClass())) {
			((HasClearButton) vaadinField).setClearButtonVisible(true);
		}

		vaadinField.setEnabled(crudField.isEnabled());
	}

}
