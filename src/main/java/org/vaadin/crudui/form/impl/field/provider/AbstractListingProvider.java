package org.vaadin.crudui.form.impl.field.provider;

import java.util.Collection;

import org.vaadin.crudui.form.FieldProvider;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.data.binder.HasDataProvider;

/**
 * @author Alejandro Duarte
 */
public abstract class AbstractListingProvider<C extends Component & HasDataProvider<T> & HasValue<ComponentValueChangeEvent<C, T>, T>, T>
        implements FieldProvider<C, T> {

    protected String caption;
    protected Collection<T> items;
    protected ItemLabelGenerator<T> itemCaptionGenerator;

    protected abstract C buildAbstractListing();

    public AbstractListingProvider(Collection<T> items) {
        this(null, items, t -> t == null ? "" : t.toString());
    }

    public AbstractListingProvider(String caption, Collection<T> items) {
        this(caption, items, t -> t == null ? "" : t.toString());
    }

    public AbstractListingProvider(String caption, Collection<T> items, ItemLabelGenerator<T> itemCaptionGenerator) {
        this.caption = caption;
        this.items = items;
        this.itemCaptionGenerator = itemCaptionGenerator;
    }

    @Override
    public HasValue<ComponentValueChangeEvent<C, T>, T> buildField() {
        C field = buildAbstractListing();
        // FIXME missing feature setCaption
        // field.setCaption(caption);
        field.setItems(items);
        return field;
    }

}
