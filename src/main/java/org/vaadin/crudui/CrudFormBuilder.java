package org.vaadin.crudui;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

import java.io.Serializable;

/**
 * @author Alejandro Duarte
 */
public interface CrudFormBuilder<T> extends Serializable {

    Component buildNewForm(T domainObject, Object visiblePropertyIds[], Object disabledPropertyIds[], String[] fieldCaptions, boolean readOnly, String buttonCaption, Resource buttonIcon, String buttonStyle, Button.ClickListener saveButtonClickListener);

}
