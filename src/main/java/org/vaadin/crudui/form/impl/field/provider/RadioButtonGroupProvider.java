package org.vaadin.crudui.form.impl.field.provider;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public class RadioButtonGroupProvider<T> extends AbstractListingProvider<RadioButtonGroup<T>, T> {

    public RadioButtonGroupProvider(Collection<T> items) {
        super(items);
    }

    public RadioButtonGroupProvider(String caption, Collection<T> items) {
        super(caption, items);
    }

    public RadioButtonGroupProvider(String caption, Collection<T> items, ItemLabelGenerator<T> itemCaptionGenerator) {
        super(caption, items, itemCaptionGenerator);
    }

    @Override
    protected RadioButtonGroup<T> buildAbstractListing() {
        RadioButtonGroup<T> field = new RadioButtonGroup<>();
        field.setItems(items);
        // FIXME missing ItemLabelGenerator for RadioButtonGroup
        return field;
    }

}
