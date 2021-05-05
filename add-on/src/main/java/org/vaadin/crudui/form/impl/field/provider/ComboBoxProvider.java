package org.vaadin.crudui.form.impl.field.provider;

import java.util.Collection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.vaadin.crudui.form.FieldProvider;

/**
 * @author Alejandro Duarte
 */
public class ComboBoxProvider<T> implements FieldProvider<ComboBox<T>> {

    private ItemLabelGenerator<T> itemLabelGenerator;
    private ComponentRenderer<? extends Component, T> renderer;
    protected Collection<T> items;

    public ComboBoxProvider(Collection<T> items) {
       this(items,null,null);
    }

    public ComboBoxProvider(Collection<T> items, ComponentRenderer<? extends Component, T> renderer,ItemLabelGenerator<T> itemLabelGenerator) {
        this.items = items;
        this.renderer = renderer;
        this.itemLabelGenerator = itemLabelGenerator;
    }

    @Override
    public ComboBox<T> buildField() {

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
