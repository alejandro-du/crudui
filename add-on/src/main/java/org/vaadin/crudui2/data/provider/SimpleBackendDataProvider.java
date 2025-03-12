package org.vaadin.crudui2.data.provider;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Stream;

import com.vaadin.flow.data.provider.AbstractDataProvider;
import com.vaadin.flow.data.provider.Query;

public class SimpleBackendDataProvider<B> extends AbstractDataProvider<B, Void> {

	public interface ItemsProvider<B> extends Serializable {

		Collection<B> getItems();
	}

	private ItemsProvider<B> itemsProvider;

	public SimpleBackendDataProvider(ItemsProvider<B> itemsProvider) {
		this.itemsProvider = itemsProvider;
	}

	@Override
	public boolean isInMemory() {
		return false;
	}

	@Override
	public int size(Query<B, Void> query) {
		return itemsProvider.getItems().size();
	}

	@Override
	public Stream<B> fetch(Query<B, Void> query) {
		query.getLimit();
		query.getOffset();
		return itemsProvider.getItems().stream().skip(query.getOffset()).limit(query.getLimit());
	}

}
