package org.vaadin.crudui.crud;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Alejandro Duarte.
 */
public enum CrudOperation {

    READ, ADD, UPDATE, DELETE;

    public static Stream<CrudOperation> stream() {
        return Arrays.stream(values());
    }

}
