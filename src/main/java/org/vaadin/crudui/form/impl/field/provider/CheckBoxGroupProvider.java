package org.vaadin.crudui.form.impl.field.provider;


import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.checkbox.CheckboxGroup;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public class CheckBoxGroupProvider<T> extends AbstractListingProvider<CheckboxGroup<T>, T> {

    private ItemLabelGenerator<T> itemLabelGenerator;

    public CheckBoxGroupProvider(Collection<T> items) {
        super(items);
    }

    public CheckBoxGroupProvider(String caption, Collection<T> items) {
        super(caption, items);
    }

    public CheckBoxGroupProvider(String caption, Collection<T> items, ItemLabelGenerator<T> itemLabelGenerator) {
        super(caption, items);
        this.itemLabelGenerator = itemLabelGenerator;
    }

    @Override
    protected CheckboxGroup<T> buildAbstractListing() {
        CheckboxGroup<T> field = new CheckboxGroup<>();
        if (itemLabelGenerator != null) {
            field.setItemLabelGenerator(itemLabelGenerator);
        }
        return field;
    }

}
