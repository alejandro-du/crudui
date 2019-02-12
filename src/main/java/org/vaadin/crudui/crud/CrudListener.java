package org.vaadin.crudui.crud;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Alejandro Duarte
 */
public interface CrudListener<T> extends Serializable {

    Collection<T> findAll();

    T add(T domainObjectToAdd);

    /**
     * Default implementation uses Jackson-ObjectMapper.
     *
     * @param domainObjectToCopy
     * @return New Object with copied properties from domainObjectToCopy or original
     *         object if error occured
     */
    default T preCopy(T domainObjectToCopy, Class<T> clazz) {
	final ObjectMapper om = new ObjectMapper();
	try {
	    return om.readValue(om.writeValueAsString(domainObjectToCopy), clazz);
	} catch (final IOException e) {
	    e.printStackTrace();
	}
	return domainObjectToCopy;
    };

    T update(T domainObjectToUpdate);

    void delete(T domainObjectToDelete);

}
