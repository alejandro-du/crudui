package org.vaadin.crudui.form.impl.field.provider;

import java.util.Collection;

import org.vaadin.crudui.form.FieldProvider;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.data.provider.HasListDataView;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;

/**
 * @author Alejandro Duarte
 */
public abstract class AbstractListingProvider<C extends Component & HasListDataView<T, ?> & HasValueAndElement, T>
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
    public C buildField(T t) {
        C field = buildAbstractListing();
        field.setItems(items);
        return field;
    }

}
