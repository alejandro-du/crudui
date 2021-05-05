package org.vaadin.crudui.form.impl.field.provider;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.vaadin.crudui.form.FieldProvider;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public class RadioButtonGroupProvider<T> implements FieldProvider<RadioButtonGroup<T>> {
    private ComponentRenderer<? extends Component, T> renderer;
    protected Collection<T> items;

    public RadioButtonGroupProvider(Collection<T> items) {
        this(items,null);
    }


    public RadioButtonGroupProvider(Collection<T> items, ComponentRenderer<? extends Component, T> renderer) {
        this.items = items;
        this.renderer = renderer;
    }

    @Override
    public RadioButtonGroup<T> buildField() {
        RadioButtonGroup<T> field = new RadioButtonGroup<>();
        if(renderer != null) {
            field.setRenderer(renderer);
        }
        field.setItems(items);
        return field;
    }

}
