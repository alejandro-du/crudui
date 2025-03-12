package org.vaadin.crudui2.list;

import java.io.Serializable;

import com.vaadin.flow.data.provider.DataProvider;

public interface CrudList<B> {

	@FunctionalInterface
	public interface ItemSelectedListener<T> extends Serializable {

		void onItemSelected(T selectedItem);

	}

	void setDataProvider(DataProvider<B, ?> dataProvider);

	void addItemSelectedListener(ItemSelectedListener<B> listener);

	void refreshItem(B item);

	void refreshAllItems();

}
