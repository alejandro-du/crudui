package org.vaadin.crudui.form.impl.field.provider;


import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.selection.MultiSelect;
import com.vaadin.flow.data.selection.MultiSelectionListener;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import org.vaadin.crudui.form.FieldProvider;

import java.util.Collection;
import java.util.Set;

/**
 * @author Alejandro Duarte
 */
public class CheckBoxGroupProvider<T> implements FieldProvider {

    public class CheckBoxGroup<T> extends Grid<T> {

        public void setLabel(String text) {
            asMultiSelect().setLabel(text);
        }

        public class CheckBoxGroupMultiSelect implements MultiSelect<Grid<T>, T> {

            private MultiSelect<Grid<T>, T> multiSelect;

            public CheckBoxGroupMultiSelect(MultiSelect<Grid<T>, T> multiSelect) {
                this.multiSelect = multiSelect;
            }

            @Override
            public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<Grid<T>, Set<T>>> valueChangeListener) {
                return multiSelect.addValueChangeListener(valueChangeListener);
            }

            @Override
            public Element getElement() {
                return multiSelect.getElement();
            }

            @Override
            public void updateSelection(Set<T> addedItems, Set<T> removedItems) {
                multiSelect.updateSelection(addedItems, removedItems);
            }

            @Override
            public Set<T> getSelectedItems() {
                return multiSelect.getSelectedItems();
            }

            @Override
            public Registration addSelectionListener(MultiSelectionListener<Grid<T>, T> listener) {
                return addSelectionListener(listener);
            }

            public void setLabel(String text) {
                getColumns().get(0).setHeader(text);
            }
        }

        public CheckBoxGroupMultiSelect asMultiSelect() {
            return new CheckBoxGroupMultiSelect(super.asMultiSelect());
        }

    }

    private CheckBoxGroup<T> checkBoxGroup = new CheckBoxGroup<>();

    public CheckBoxGroupProvider(Collection<T> items) {
        this(null, items, t -> t == null ? "" : t.toString());
    }

    public CheckBoxGroupProvider(String label, Collection<T> items) {
        this(label, items, t -> t == null ? "" : t.toString());
    }

    public CheckBoxGroupProvider(String label, Collection<T> items, ValueProvider<T, String> itemCaptionGenerator) {
        checkBoxGroup.setItems(items);
        checkBoxGroup.setSelectionMode(Grid.SelectionMode.MULTI);
        checkBoxGroup.addColumn(itemCaptionGenerator::apply);
        checkBoxGroup.setHeightByRows(true);
        checkBoxGroup.getHeaderRows().clear();
        if (label != null) {
            checkBoxGroup.setLabel(label);
        }
    }

    @Override
    public HasValueAndElement buildField() {
        return checkBoxGroup.asMultiSelect();
    }

}