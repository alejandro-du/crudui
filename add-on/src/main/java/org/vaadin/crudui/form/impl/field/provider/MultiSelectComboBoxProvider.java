package org.vaadin.crudui.form.impl.field.provider;

import java.util.Collection;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;

public class MultiSelectComboBoxProvider<T> extends AbstractListingProvider<MultiSelectComboBox<T>, T> {

	private ItemLabelGenerator<T> itemLabelGenerator;

	public MultiSelectComboBoxProvider(Collection<T> items) {
		super(items);
	}

	public MultiSelectComboBoxProvider(String caption, Collection<T> items) {
		super(caption, items);
	}

	public MultiSelectComboBoxProvider(String caption, Collection<T> items, ItemLabelGenerator<T> itemLabelGenerator) {
		super(caption, items);
		this.itemLabelGenerator = itemLabelGenerator;
	}

	@Override
	protected MultiSelectComboBox<T> buildAbstractListing() {
		MultiSelectComboBox<T> field = new MultiSelectComboBox<>();
		if (itemLabelGenerator != null) {
			field.setItemLabelGenerator(itemLabelGenerator);
		}
		return field;
	}

}
