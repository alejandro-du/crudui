package org.vaadin.crudui;

import com.vaadin.ui.Component;

/**
 * @author Alejandro Duarte
 */
public interface CrudLayout extends Component {

    void setMainComponent(Component component);

    void addFilterComponent(Component component);

    void addToolbarComponent(Component component);

}
