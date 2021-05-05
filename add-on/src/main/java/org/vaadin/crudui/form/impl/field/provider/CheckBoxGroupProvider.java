package org.vaadin.crudui.form.impl.field.provider;


import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import org.vaadin.crudui.form.FieldProvider;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public class CheckBoxGroupProvider<T> implements FieldProvider<CheckboxGroup<T>> {

    private ItemLabelGenerator<T> itemLabelGenerator;
    private Collection<T> items;

    public CheckBoxGroupProvider(Collection<T> items) {
        this(items, null);
    }

    public CheckBoxGroupProvider(Collection<T> items, ItemLabelGenerator<T> itemLabelGenerator) {
        this.items = items;
        this.itemLabelGenerator = itemLabelGenerator;
    }

    @Override
    public CheckboxGroup<T> buildField() {
        CheckboxGroup<T> field = new CheckboxGroup<>();
        if (itemLabelGenerator != null) {
            field.setItemLabelGenerator(itemLabelGenerator);
        }
        return field;
    }
}
