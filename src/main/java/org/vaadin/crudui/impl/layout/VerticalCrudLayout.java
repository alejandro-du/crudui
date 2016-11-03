package org.vaadin.crudui.impl.layout;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.crudui.CrudLayout;

/**
 * @author Alejandro Duarte
 */
public class VerticalCrudLayout extends CustomComponent implements CrudLayout {

    private VerticalLayout mainLayout = new VerticalLayout();
    private Label captionLabel = new Label();
    private HorizontalLayout headerLayout = new HorizontalLayout();
    private HorizontalLayout toolbarLayout = new HorizontalLayout();
    private HorizontalLayout filterLayout = new HorizontalLayout();
    private VerticalLayout mainComponentLayout = new VerticalLayout();

    public VerticalCrudLayout() {
        mainLayout.setSizeFull();
        mainLayout.setSpacing(true);
        setCompositionRoot(mainLayout);
        setSizeFull();

        captionLabel.addStyleName(ValoTheme.LABEL_COLORED);
        captionLabel.addStyleName(ValoTheme.LABEL_BOLD);
        captionLabel.addStyleName(ValoTheme.LABEL_H3);
        captionLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        captionLabel.setVisible(false);

        headerLayout.setVisible(false);
        headerLayout.setSpacing(true);

        toolbarLayout.setVisible(false);
        toolbarLayout.setSpacing(true);
        headerLayout.addComponent(toolbarLayout);

        filterLayout.setVisible(false);
        filterLayout.setSpacing(true);
        headerLayout.addComponent(filterLayout);

        Label filterIcon = new Label();
        filterIcon.setIcon(FontAwesome.SEARCH);
        filterIcon.setDescription("Use los campos para filtrar");
        filterLayout.addComponent(filterIcon);

        mainComponentLayout.setSizeFull();
        mainLayout.addComponent(mainComponentLayout);
        mainLayout.setExpandRatio(mainComponentLayout, 1);
    }

    @Override
    public void setCaption(String caption) {
        if (!captionLabel.isVisible()) {
            captionLabel.setVisible(true);
            mainLayout.addComponent(captionLabel, 0);
        }

        captionLabel.setValue(caption);
    }

    @Override
    public void addToolbarComponent(Component component) {
        if (!headerLayout.isVisible()) {
            headerLayout.setVisible(true);
            mainLayout.addComponent(headerLayout, mainLayout.getComponentCount() - 1);
        }

        toolbarLayout.setVisible(true);
        toolbarLayout.addComponent(component);
    }

    @Override
    public void setMainComponent(Component component) {
        mainComponentLayout.removeAllComponents();
        mainComponentLayout.addComponent(component);
    }

    @Override
    public void addFilterComponent(Component component) {
        if (!headerLayout.isVisible()) {
            headerLayout.setVisible(true);
            mainLayout.addComponent(headerLayout, mainLayout.getComponentCount() - 1);
        }

        filterLayout.setVisible(true);
        filterLayout.addComponent(component);
    }

}
