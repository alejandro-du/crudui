package org.vaadin.crudui2.form.field.provider;

import java.util.Collection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class MultiSelectComboBoxProvider<V> implements FieldProvider {

	private final Collection<V> items;
	private final ItemLabelGenerator<V> itemLabelGenerator;
	private final ComponentRenderer<? extends Component, V> renderer;

	public MultiSelectComboBoxProvider(Collection<V> items, ComponentRenderer<? extends Component, V> renderer,
			ItemLabelGenerator<V> itemLabelGenerator) {
		this.items = items;
		this.renderer = renderer;
		this.itemLabelGenerator = itemLabelGenerator;
	}

	public MultiSelectComboBoxProvider(Collection<V> items, ItemLabelGenerator<V> itemLabelGenerator) {
		this(items, null, itemLabelGenerator);
	}

	public MultiSelectComboBoxProvider(Collection<V> items) {
		this(items, null, null);
	}

	@Override
	public MultiSelectComboBox<V> buildField() {
		MultiSelectComboBox<V> field = new MultiSelectComboBox<>();
		if (itemLabelGenerator != null) {
			field.setItemLabelGenerator(itemLabelGenerator);
		}
		if (renderer != null) {
			field.setRenderer(renderer);
		}
		field.setItems(items);
		return field;
	}

}
