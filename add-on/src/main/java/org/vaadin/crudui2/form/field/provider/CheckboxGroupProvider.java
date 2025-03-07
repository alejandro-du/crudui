package org.vaadin.crudui2.form.field.provider;

import java.util.Collection;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

public class CheckboxGroupProvider<B, V> implements FieldProvider<B, AbstractField<?, V>, V> {

	private final SerializableFunction<B, Collection<V>> itemsSupplier;
	private final ItemLabelGenerator<V> itemLabelGenerator;
	private final ComponentRenderer<? extends Component, V> renderer;

	public CheckboxGroupProvider(SerializableFunction<B, Collection<V>> itemsSupplier,
			ComponentRenderer<? extends Component, V> renderer, ItemLabelGenerator<V> itemLabelGenerator) {
		this.itemsSupplier = itemsSupplier;
		this.renderer = renderer;
		this.itemLabelGenerator = itemLabelGenerator;
	}

	public CheckboxGroupProvider(SerializableFunction<B, Collection<V>> itemsSupplier,
			ItemLabelGenerator<V> itemLabelGenerator) {
		this(itemsSupplier, null, itemLabelGenerator);
	}

	public CheckboxGroupProvider(SerializableFunction<B, Collection<V>> itemsSupplier) {
		this(itemsSupplier, null, null);
	}

	@Override
	public AbstractField<?, V> buildField(B bean) {
		CheckboxGroup<V> field = new CheckboxGroup<>();
		if (itemLabelGenerator != null) {
			field.setItemLabelGenerator(itemLabelGenerator);
		}
		if (renderer != null) {
			field.setRenderer(renderer);
		}
		field.setItems(itemsSupplier.apply(bean));
		return (AbstractField<?, V>) field;
	}

}
