package org.vaadin.crudui.form.impl.field.provider;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.vaadin.pekka.CheckboxGroup;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public class CheckBoxGroupProvider<T> extends AbstractListingProvider<CheckboxGroup<T>, T> {

    public CheckBoxGroupProvider(Collection<T> items) {
        super(items);
    }

    public CheckBoxGroupProvider(String caption, Collection<T> items) {
        super(caption, items);
    }

    public CheckBoxGroupProvider(String caption, Collection<T> items, ComponentRenderer<? extends Component, T> renderer) {
        super(caption, items, renderer);
    }

    @Override
    protected CheckboxGroup<T> buildAbstractListing() {
        CheckboxGroup<T> field = new CheckboxGroup<>();
        field.getElement().getStyle().set("flexDirection", "column");
        if(renderer != null) {
            field.setRenderer(renderer);
        }
        return field;
    }
}