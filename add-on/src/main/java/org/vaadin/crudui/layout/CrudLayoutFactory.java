package org.vaadin.crudui.layout;

/**
 * Factory interface for creating CrudLayout implementations.
 * Implementations like SplitCrudLayout.Builder use this to build configured layout components.
 *
 * @param <B> The bean type
 */
public interface CrudLayoutFactory<B> {

    /**
     * Builds and returns a CrudLayout instance.
     *
     * @return A configured CrudLayout
     */
    CrudLayout<B> build();

}
