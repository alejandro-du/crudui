package org.vaadin.crudui.form.impl.field.provider;

import java.util.Collection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.ComponentRenderer;

/**
 * @author Alejandro Duarte
 */
public class ComboBoxProvider<T> extends AbstractListingProvider<ComboBox<T>, T> {

    private ItemLabelGenerator<T> itemLabelGenerator;

    public ComboBoxProvider(Collection<T> items) {
        super(items);
    }

    public ComboBoxProvider(String caption, Collection<T> items) {
        super(caption, items);
    }

    public ComboBoxProvider(String caption, Collection<T> items, ComponentRenderer<? extends Component, T> renderer, ItemLabelGenerator<T> itemLabelGenerator) {
        super(caption, items, renderer);
        this.itemLabelGenerator = itemLabelGenerator;
    }

    @Override
    protected ComboBox<T> buildAbstractListing() {
        ComboBox<T> field = new ComboBox<>();
        if(renderer != null) {
            field.setRenderer(renderer);
        }
        if (itemLabelGenerator != null) {
            field.setItemLabelGenerator(itemLabelGenerator);
        }
        field.setItems(items);
        return field;
    }

}
