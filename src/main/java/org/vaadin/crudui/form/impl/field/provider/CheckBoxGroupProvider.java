package org.vaadin.crudui.form.impl.field.provider;

import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.ItemCaptionGenerator;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public class CheckBoxGroupProvider<T> extends AbstractListingProvider<CheckBoxGroup<T>, T> {

    public CheckBoxGroupProvider(Collection<T> items) {
        super(items);
    }

    public CheckBoxGroupProvider(String caption, Collection<T> items) {
        super(caption, items);
    }

    public CheckBoxGroupProvider(String caption, Collection<T> items, ItemCaptionGenerator<T> itemCaptionGenerator) {
        super(caption, items, itemCaptionGenerator);
    }

    @Override
    protected CheckBoxGroup<T> buildAbstractListing() {
        CheckBoxGroup<T> field = new CheckBoxGroup<>(caption, items);
        field.setItemCaptionGenerator(itemCaptionGenerator);
        return field;
    }

}
