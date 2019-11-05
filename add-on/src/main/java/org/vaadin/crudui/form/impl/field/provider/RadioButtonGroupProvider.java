package org.vaadin.crudui.form.impl.field.provider;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.renderer.ComponentRenderer;

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

    public RadioButtonGroupProvider(String caption, Collection<T> items, ComponentRenderer<? extends Component, T> renderer) {
        super(caption, items, renderer);
    }

    @Override
    protected RadioButtonGroup<T> buildAbstractListing() {
        RadioButtonGroup<T> field = new RadioButtonGroup<>();
        if(renderer != null) {
            field.setRenderer(renderer);
        }
        field.setItems(items);
        return field;
    }

}
