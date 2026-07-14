package org.vaadin.crudui2;

import java.util.List;

/**
 * Listener interface for CRUD operations. Implement this to connect a CRUD component
 * to backend services for create, read, update, and delete operations.
 *
 * @param <B> The bean type managed by the CRUD
 */
public interface CrudListener<B> {

    /**
     * Called to retrieve all beans for display in the list.
     *
     * @return A list of all beans
     */
    List<B> onRead();

    /**
     * Called when the user creates a new bean.
     *
     * @param bean The new bean to create
     */
    void onCreate(B bean);

    /**
     * Called when the user saves/updates an existing bean.
     *
     * @param bean The bean to update
     */
    void onUpdate(B bean);

    /**
     * Called when the user deletes a bean.
     *
     * @param bean The bean to delete
     */
    void onDelete(B bean);

}
