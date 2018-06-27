package org.vaadin.crudui.form.impl.field.provider;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.data.binder.HasDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import org.vaadin.crudui.form.FieldProvider;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public abstract class AbstractListingProvider<C extends Component & HasDataProvider<T> & HasValueAndElement, T>
        implements FieldProvider<C, T> {

    protected String caption;
    protected Collection<T> items;
    protected ComponentRenderer<? extends Component, T> renderer;

    protected abstract C buildAbstractListing();

    public AbstractListingProvider(Collection<T> items) {
        this(null, items, new TextRenderer<>());
    }

    public AbstractListingProvider(String caption, Collection<T> items) {
        this(caption, items, new TextRenderer<>());
    }

    public AbstractListingProvider(String caption, Collection<T> items, ComponentRenderer<? extends Component, T> renderer) {
        this.caption = caption;
        this.items = items;
        this.renderer = renderer;
    }

    @Override
    public HasValueAndElement<ComponentValueChangeEvent<C, T>, T> buildField() {
        C field = buildAbstractListing();
        field.setItems(items);
        return field;
    }

}
