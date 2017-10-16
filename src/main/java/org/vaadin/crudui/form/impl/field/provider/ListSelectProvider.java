package org.vaadin.crudui.form.impl.field.provider;

import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.ListSelect;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public class ListSelectProvider<T> extends AbstractListingProvider<ListSelect<T>, T> {

    public ListSelectProvider(Collection<T> items) {
        super(items);
    }

    public ListSelectProvider(String caption, Collection<T> items) {
        super(caption, items);
    }

    public ListSelectProvider(String caption, Collection<T> items, ItemCaptionGenerator<T> itemCaptionGenerator) {
        super(caption, items, itemCaptionGenerator);
    }

    @Override
    protected ListSelect<T> buildAbstractListing() {
        ListSelect<T> field = new ListSelect<>(caption, items);
        field.setItemCaptionGenerator(itemCaptionGenerator);
        return field;
    }

}
