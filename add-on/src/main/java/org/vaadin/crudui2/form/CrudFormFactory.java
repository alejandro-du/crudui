package org.vaadin.crudui2.form;

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

import org.vaadin.crudui2.form.CrudField.Builder;
import org.vaadin.crudui2.form.CrudField.UpdateHandler;
import org.vaadin.crudui2.form.provider.FieldProvider;
import org.vaadin.crudui2.form.provider.TypeBasedFieldProvider;

public class CrudFormFactory<B> {

	private Class<B> domainType;
	private List<CrudField.Builder<?, ?, ?>> fieldBuilders = new ArrayList<>();
	private boolean useBeanValidation;

	public static <B> CrudFormFactory<B> of(Class<B> domainType) {
		return new CrudFormFactory<>(domainType);
	}

	private CrudFormFactory(Class<B> domainType) {
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

	public CrudFormFactory<B> replace(String propertyName, CrudField.Builder<?, ?, ?> newFieldBuilder) {
		fieldBuilders.replaceAll(builder -> {
			if (propertyName.equals(builder.propertyName)) {
				return newFieldBuilder;
			}
			return builder;
		});
		return this;
	}

	public CrudFormFactory<B> useBeanValidation() {
		useBeanValidation = true;
		return this;
	}

	public CrudForm<B> build(final B bean) {
		CrudForm<B> form = new CrudForm<>(domainType, useBeanValidation);
		form.setValue(bean);

		var vaadinFieldByBuilder = new HashMap<Builder<?, ?, ?>, AbstractField<?, ?>>();
		var crudFieldByVaadinField = new HashMap<AbstractField<?, ?>, CrudField<B, ?, ?>>();
		var vaadinFieldByCrudField = new HashMap<CrudField<B, ?, ?>, AbstractField<?, ?>>();
		var notifiers = new HashMap<AbstractField<?, ?>, List<Builder<B, ?, ?>>>();

		fieldBuilders.forEach(builder -> {
			try {
				CrudField crudField = builder.build();
				FieldProvider fieldProvider = crudField.getFieldProvider();

				if (fieldProvider == null) {
					fieldProvider = new TypeBasedFieldProvider<>(crudField.getFieldType(),
							crudField.getFieldValueType(), crudField.getPropertyName());
				}

				AbstractField vaadinField = fieldProvider.buildField(bean);
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
			} catch (TypeBasedFieldProvider.UnsupportedFieldTypeException ignored) {
				// no field is created
			}
		});

		addNotifiers(form, vaadinFieldByBuilder, crudFieldByVaadinField, notifiers);
		updateFields(form, crudFieldByVaadinField, vaadinFieldByCrudField);

		return form;
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

	private void updateFields(CrudForm<B> form, HashMap<AbstractField<?, ?>, CrudField<B, ?, ?>> crudFieldByVaadinField,
			HashMap<CrudField<B, ?, ?>, AbstractField<?, ?>> vaadinFieldByCrudField) {
		form.addValueChangeListener(event -> {
			crudFieldByVaadinField.values().forEach(crudField -> {
				if (crudField.getUpdateHandler() != null) {
					AbstractField<?, ?> vaadinField = vaadinFieldByCrudField.get(crudField);
					B value = event.getValue();
					crudField.getUpdateHandler().onUpdate(vaadinField, value);
				}
			});
		});
	}

	private void addNotifiers(CrudForm<B> form, HashMap<Builder<?, ?, ?>, AbstractField<?, ?>> vaadinFieldByBuilder,
			HashMap<AbstractField<?, ?>, CrudField<B, ?, ?>> crudFieldByVaadinField,
			HashMap<AbstractField<?, ?>, List<Builder<B, ?, ?>>> notifiers) {
		notifiers.forEach((vaadinFieldNotifier, builderListeners) -> {
			vaadinFieldNotifier.addValueChangeListener(event -> {
				builderListeners.forEach(builderListener -> {
					B value = form.getValue();
					AbstractField<?, ?> vaadinField = vaadinFieldByBuilder.get(builderListener);
					CrudField crudField = crudFieldByVaadinField.get(vaadinField);
					UpdateHandler<B> updateHandler = crudField.getUpdateHandler();
					if (updateHandler != null) {
						updateHandler.onUpdate(vaadinField, value);
					}
				});
			});
		});
	}

	private void autoGenerateFieldBuilders() {
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructType(domainType);
		var beanDescription = (BasicBeanDescription) mapper.getSerializationConfig().introspect(javaType);
		beanDescription.findProperties().forEach(property -> {
			String propertyName = property.getName();
			var fieldBuilder = CrudField.of(propertyName);
			fieldBuilder.label(SharedUtil.propertyIdToHumanFriendly(propertyName));
			fieldBuilders.add(fieldBuilder);
		});
	}

}
