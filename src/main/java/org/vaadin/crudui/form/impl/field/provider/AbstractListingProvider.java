package org.vaadin.crudui.form.impl.field.provider;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractListing;
import com.vaadin.ui.ItemCaptionGenerator;
import org.vaadin.crudui.form.FieldProvider;

import java.util.Collection;

/**
 * @author Alejandro Duarte
 */
public abstract class AbstractListingProvider<F extends AbstractListing, T> implements FieldProvider {

    protected String caption;
    protected Collection<T> items;
    protected ItemCaptionGenerator<T> itemCaptionGenerator;

    protected abstract F buildAbstractListing();

    public AbstractListingProvider(Collection<T> items) {
        this(null, items, t -> t == null ? "" : t.toString());
    }

    public AbstractListingProvider(String caption, Collection<T> items) {
        this(caption, items, t -> t == null ? "" : t.toString());
    }

    public AbstractListingProvider(String caption, Collection<T> items, ItemCaptionGenerator<T> itemCaptionGenerator) {
        this.caption = caption;
        this.items = items;
        this.itemCaptionGenerator = itemCaptionGenerator;
    }

    @Override
    public HasValue buildField() {
        F field = buildAbstractListing();
        field.setCaption(caption);
        field.setItems(items);
        return (HasValue) field;
    }

}
