package org.vaadin.crudui.crud.impl;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.function.ValueProvider;
import java.util.Collection;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.LazyFindAllCrudOperationListener;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.layout.CrudLayout;

/**
 * @author Boniface Chacha
 */
public class TreeGridCrud<T> extends AbstractGridCrud<T> {

  private ValueProvider<T, Collection<T>> childItemProvider;

  public TreeGridCrud(Class<T> domainType) {
    super(domainType);
  }

  public TreeGridCrud(Class<T> domainType, CrudLayout crudLayout) {
    super(domainType, crudLayout);
  }

  public TreeGridCrud(Class<T> domainType, CrudFormFactory<T> crudFormFactory) {
    super(domainType, crudFormFactory);
  }

  public TreeGridCrud(Class<T> domainType, CrudListener<T> crudListener) {
    super(domainType, crudListener);
  }

  public TreeGridCrud(Class<T> domainType, CrudLayout crudLayout, CrudFormFactory<T> crudFormFactory) {
    super(domainType, crudLayout, crudFormFactory);
  }

  public TreeGridCrud(Class<T> domainType, CrudLayout crudLayout, CrudFormFactory<T> crudFormFactory, CrudListener<T> crudListener) {
    super(domainType, crudLayout, crudFormFactory, crudListener);
  }

  @Override
  protected Grid<T> createGrid() {
    return new TreeGrid<>(domainType);
  }

  @Override
  public TreeGrid<T> getGrid() {
    return (TreeGrid<T>) super.getGrid();
  }

  public void refreshGrid() {
    if (LazyFindAllCrudOperationListener.class.isAssignableFrom(findAllOperation.getClass())) {
      LazyFindAllCrudOperationListener findAll = (LazyFindAllCrudOperationListener) findAllOperation;
      DataProvider dataProvider = findAll.getDataProvider();

      if (!HierarchicalDataProvider.class.isAssignableFrom(dataProvider.getClass()))
        throw new UnsupportedOperationException("The data provider for TreeGridCrud must implement HierarchicalDataProvider");

      getGrid().setItems(dataProvider);
    } else {
      Collection<T> items = findAllOperation.findAll();
      getGrid().setItems(items, childItemProvider);
    }
  }

  public ValueProvider<T, Collection<T>> getChildItemProvider() {
    return childItemProvider;
  }

  public void setChildItemProvider(ValueProvider<T, Collection<T>> childItemProvider) {
    this.childItemProvider = childItemProvider;
  }
}
