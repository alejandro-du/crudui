package org.vaadin.crudui2;

/**
 * Listener interface for CRUD operations. Implement this to connect a CRUD component
 * to backend services for save, create, and delete operations.
 *
 * @param <B> The bean type managed by the CRUD
 */
public interface CrudListener<B> {

    /**
     * Called when the user saves an existing bean (update).
     *
     * @param bean The bean to save
     */
    void onSave(B bean);

    /**
     * Called when the user creates a new bean.
     *
     * @param bean The new bean to create
     */
    void onCreate(B bean);

    /**
     * Called when the user deletes a bean.
     *
     * @param bean The bean to delete
     */
    void onDelete(B bean);

}
