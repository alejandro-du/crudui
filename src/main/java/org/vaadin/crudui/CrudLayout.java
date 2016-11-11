package org.vaadin.crudui;

import com.vaadin.ui.Component;

/**
 * @author Alejandro Duarte
 */
public interface CrudLayout extends Component {

    void setCaption(String caption);

    void setMainComponent(Component component);

    void addFilterComponent(Component component);

    void addToolbarComponent(Component component);

    void showReadForm(String caption, Component formComponent);

    void showAddForm(String caption, Component formComponent);

    void showUpdateForm(String caption, Component formComponent);

    void showDeleteForm(String caption, Component formComponent);

    void hideForm();

}
