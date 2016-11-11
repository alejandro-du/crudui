package org.vaadin.crudui;

import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Field;
import com.vaadin.ui.OptionGroup;

import java.util.Set;

/**
 * @author Alejandro Duarte
 */
public class DefaultCrudFieldFactory extends DefaultFieldGroupFieldFactory {

    @Override
    public <T extends Field> T createField(Class<?> type, Class<T> fieldType) {
        T field = null;

        if (fieldType != Field.class) {
            try {
                field = fieldType.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        } else if (Set.class.isAssignableFrom(type)) {
            field = (T) createSetField();
        } else {
            field = super.createField(type, fieldType);
        }

        fixNullRepresentation(field);

        return field;
    }

    private OptionGroup createSetField() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setMultiSelect(true);
        return optionGroup;
    }

    public void fixNullRepresentation(Field<?> field) {
        if (field != null && AbstractTextField.class.isAssignableFrom(field.getClass())) {
            ((AbstractTextField) field).setNullRepresentation("");
        }
    }

}
