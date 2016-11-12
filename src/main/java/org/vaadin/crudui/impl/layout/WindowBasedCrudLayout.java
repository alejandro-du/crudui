package org.vaadin.crudui.impl.layout;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.crudui.CrudLayout;

/**
 * @author Alejandro Duarte
 */
public class WindowBasedCrudLayout extends CustomComponent implements CrudLayout {

    protected VerticalLayout mainLayout = new VerticalLayout();
    protected Label captionLabel = new Label();
    protected HorizontalLayout headerLayout = new HorizontalLayout();
    protected CssLayout toolbarLayout = new CssLayout();
    protected HorizontalLayout filterLayout = new HorizontalLayout();
    protected VerticalLayout mainComponentLayout = new VerticalLayout();
    protected Window formWindow;
    protected String formWindowWidth = "500px";

    public WindowBasedCrudLayout() {
        setCompositionRoot(mainLayout);
        mainLayout.setSizeFull();
        mainLayout.setSpacing(true);
        setSizeFull();

        captionLabel.addStyleName(ValoTheme.LABEL_COLORED);
        captionLabel.addStyleName(ValoTheme.LABEL_BOLD);
        captionLabel.addStyleName(ValoTheme.LABEL_H3);
        captionLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        captionLabel.setVisible(false);

        headerLayout.setVisible(false);
        headerLayout.setSpacing(true);

        toolbarLayout.setVisible(false);
        toolbarLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        headerLayout.addComponent(toolbarLayout);

        filterLayout.setVisible(false);
        filterLayout.setSpacing(true);
        headerLayout.addComponent(filterLayout);

        Label filterIcon = new Label();
        filterIcon.setIcon(FontAwesome.SEARCH);
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

    @Override
    public void addToolbarComponent(Component component) {
        if (!headerLayout.isVisible()) {
            headerLayout.setVisible(true);
            mainLayout.addComponent(headerLayout, mainLayout.getComponentCount() - 1);
        }

        toolbarLayout.setVisible(true);
        toolbarLayout.addComponent(component);
    }

    private void showWindow(String caption, Component crudForm) {
        VerticalLayout windowLayout = new VerticalLayout(crudForm);
        windowLayout.setWidth("100%");
        windowLayout.setMargin(true);

        formWindow = new Window(caption, windowLayout);
        formWindow.setWidth(formWindowWidth);
        formWindow.setModal(true);
        UI.getCurrent().addWindow(formWindow);
    }

    @Override
    public void showReadForm(String caption, Component formComponent) {

    }

    @Override
    public void showAddForm(String caption, Component formComponent) {
        showWindow(caption, formComponent);
    }

    @Override
    public void showUpdateForm(String caption, Component formComponent) {
        showWindow(caption, formComponent);
    }

    @Override
    public void showDeleteForm(String caption, Component formComponent) {
        showWindow(caption, formComponent);
    }

    @Override
    public void hideForm() {
        if (formWindow != null) {
            formWindow.close();
        }
    }

    public void setFormWindowWidth(String formWindowWidth) {
        this.formWindowWidth = formWindowWidth;
    }

}
