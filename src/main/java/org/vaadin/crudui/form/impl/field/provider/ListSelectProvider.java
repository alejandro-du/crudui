package org.vaadin.crudui.form.impl.field.provider;

import java.util.Collection;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.listbox.ListBox;

/**
 * @author Alejandro Duarte
 */
public class ListSelectProvider<T> extends AbstractListingProvider<ListBox<T>, T> {

    public ListSelectProvider(Collection<T> items) {
        super(items);
    }

    public ListSelectProvider(String caption, Collection<T> items) {
        super(caption, items);
    }

    public ListSelectProvider(String caption, Collection<T> items, ItemLabelGenerator<T> itemCaptionGenerator) {
        super(caption, items, itemCaptionGenerator);
    }

    @Override
    protected ListBox<T> buildAbstractListing() {
        ListBox<T> field = new ListBox<>();
        field.setItems(items);
        // FIXME missing item label generator for ListBox. Replace with renderer
        return field;
    }

}
