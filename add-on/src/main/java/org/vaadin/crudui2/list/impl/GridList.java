package org.vaadin.crudui2.list.impl;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;

import org.vaadin.crudui2.list.CrudList;

public class GridList<B> extends Composite<VerticalLayout> implements CrudList<B> {

	private Grid<B> grid;

	public GridList(Grid<B> grid) {
		this.grid = grid;
		getContent().add(grid);
		getContent().setPadding(false);
		getContent().setSizeFull();
	}

	@Override
	public void setDataProvider(DataProvider<B, ?> dataProvider) {
		grid.setDataProvider(dataProvider);
	}

	@Override
	public void addItemSelectedListener(ItemSelectedListener<B> listener) {
		grid.asSingleSelect().addValueChangeListener(event -> listener.onItemSelected(event.getValue()));
	}

	@Override
	public void refreshItem(B item) {
		grid.getDataProvider().refreshItem(item);
	}

	@Override
	public void refreshAllItems() {
		grid.getDataProvider().refreshAll();
	}

}
