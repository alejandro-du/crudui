package org.vaadin.crudui2.list.impl;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;

import org.vaadin.crudui2.list.CrudList;
import org.vaadin.crudui2.list.CrudListFactory;

public class GridList<B> extends Composite<VerticalLayout> implements CrudList<B> {

	private Grid<B> grid;

	/**
	 * Creates a new fluent builder for GridList.
	 *
	 * @param beanType The bean class
	 * @param <B>      The bean type
	 * @return A builder for configuring the list
	 */
	public static <B> Builder<B> of(Class<B> beanType) {
		return new Builder<>(beanType);
	}

	private GridList(Grid<B> grid) {
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

	@Override
	public void clearSelection() {
		grid.asSingleSelect().clear();
	}

	/**
	 * Fluent builder for GridList.
	 *
	 * @param <B> The bean type
	 */
	public static class Builder<B> implements CrudListFactory<B> {
		private final Class<B> beanType;
		private Grid<B> grid;
		private DataProvider<B, ?> dataProvider;

		Builder(Class<B> beanType) {
			this.beanType = beanType;
		}

		/**
		 * Sets the Grid component to use.
		 *
		 * @param grid The Grid component
		 * @return This builder for chaining
		 */
		public Builder<B> grid(Grid<B> grid) {
			this.grid = grid;
			return this;
		}

		/**
		 * Sets the data provider for the grid.
		 *
		 * @param dataProvider The data provider
		 * @return This builder for chaining
		 */
		public Builder<B> dataProvider(DataProvider<B, ?> dataProvider) {
			this.dataProvider = dataProvider;
			return this;
		}

		/**
		 * Builds and returns the GridList.
		 *
		 * @return The configured GridList
		 */
		public GridList<B> build() {
			if (grid == null) {
				grid = new Grid<>(beanType);
			}
			if (dataProvider != null) {
				grid.setDataProvider(dataProvider);
			}
			return new GridList<>(grid);
		}
	}
}
