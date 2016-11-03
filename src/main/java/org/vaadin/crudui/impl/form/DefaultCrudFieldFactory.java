package org.vaadin.crudui.impl.form;

import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Field;

/**
 * @author Alejandro Duarte
 */
public class DefaultCrudFieldFactory extends DefaultFieldGroupFieldFactory {

    @Override
    public <T extends Field> T createField(Class<?> type, Class<T> fieldType) {
        T field = super.createField(type, fieldType);

        if (AbstractTextField.class.isAssignableFrom(field.getClass())) {
            AbstractTextField textField = (AbstractTextField) field;
            textField.setNullRepresentation("");
        }

        return field;
    }

}
