package org.vaadin.crudui2.form.field.provider;

import java.util.Collection;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBoxBase;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

public class MultiSelectComboBoxProvider<B, V> implements FieldProvider<B, AbstractField<?, V>, V> {

	private final SerializableFunction<B, Collection<V>> itemsSupplier;
	private final ItemLabelGenerator<V> itemLabelGenerator;
	private final ComponentRenderer<? extends Component, V> renderer;

	public MultiSelectComboBoxProvider(SerializableFunction<B, Collection<V>> itemsSupplier,
			ComponentRenderer<? extends Component, V> renderer, ItemLabelGenerator<V> itemLabelGenerator) {
		this.itemsSupplier = itemsSupplier;
		this.renderer = renderer;
		this.itemLabelGenerator = itemLabelGenerator;
	}

	public MultiSelectComboBoxProvider(SerializableFunction<B, Collection<V>> itemsSupplier,
			ItemLabelGenerator<V> itemLabelGenerator) {
		this(itemsSupplier, null, itemLabelGenerator);
	}

	public MultiSelectComboBoxProvider(SerializableFunction<B, Collection<V>> itemsSupplier) {
		this(itemsSupplier, null, null);
	}

	@Override
	public ComboBoxBase<?, V, V> buildField(B bean) {
		MultiSelectComboBox<V> field = new MultiSelectComboBox<>();
		if (itemLabelGenerator != null) {
			field.setItemLabelGenerator(itemLabelGenerator);
		}
		if (renderer != null) {
			field.setRenderer(renderer);
		}
		field.setItems(itemsSupplier.apply(bean));
		return (ComboBoxBase<?, V, V>) field;
	}

}
