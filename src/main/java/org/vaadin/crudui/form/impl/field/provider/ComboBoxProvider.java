package org.vaadin.crudui.form.impl.field.provider;

import java.util.Collection;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;

/**
 * @author Alejandro Duarte
 */
public class ComboBoxProvider<T> extends AbstractListingProvider<ComboBox<T>, T> {

    public ComboBoxProvider(Collection<T> items) {
        super(items);
    }

    public ComboBoxProvider(String caption, Collection<T> items) {
        super(caption, items);
    }

    public ComboBoxProvider(String caption, Collection<T> items, ItemLabelGenerator<T> itemCaptionGenerator) {
        super(caption, items, itemCaptionGenerator);
    }

    @Override
    protected ComboBox<T> buildAbstractListing() {
        ComboBox<T> field = new ComboBox<>(caption, items);
        field.setItemLabelGenerator(itemCaptionGenerator);
        return field;
    }

}
