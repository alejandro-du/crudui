package org.vaadin.crudui.list;

/**
 * Factory interface for creating CrudList implementations.
 * Implementations like GridList.Builder use this to build configured list components.
 *
 * @param <B> The bean type
 */
public interface CrudListFactory<B> {

    /**
     * Builds and returns a CrudList instance.
     *
     * @return A configured CrudList
     */
    CrudList<B> build();

}
