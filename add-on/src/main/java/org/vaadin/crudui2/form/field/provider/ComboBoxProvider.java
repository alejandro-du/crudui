package org.vaadin.crudui2.form.field.provider;

import java.util.Collection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class ComboBoxProvider<V> implements FieldProvider<ComboBox<V>, V> {

	private final Collection<V> items;
	private final ItemLabelGenerator<V> itemLabelGenerator;
	private final ComponentRenderer<? extends Component, V> renderer;

	public ComboBoxProvider(Collection<V> items, ComponentRenderer<? extends Component, V> renderer,
			ItemLabelGenerator<V> itemLabelGenerator) {
		this.items = items;
		this.renderer = renderer;
		this.itemLabelGenerator = itemLabelGenerator;
	}

	public ComboBoxProvider(Collection<V> items) {
		this(items, null, null);
	}

	@Override
	public ComboBox<V> buildField() {
		ComboBox<V> field = new ComboBox<>();
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
