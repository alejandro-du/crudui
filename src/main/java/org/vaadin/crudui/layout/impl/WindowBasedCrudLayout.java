package org.vaadin.crudui.layout.impl;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.layout.CrudLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alejandro Duarte
 */
public class WindowBasedCrudLayout extends Composite<VerticalLayout> implements CrudLayout, HasSize {

    protected VerticalLayout mainLayout = new VerticalLayout();
    protected H3 captionLabel = new H3();
    protected HorizontalLayout headerLayout = new HorizontalLayout();
    protected HorizontalLayout toolbarLayout = new HorizontalLayout();
    protected HorizontalLayout filterLayout = new HorizontalLayout();
    protected VerticalLayout mainComponentLayout = new VerticalLayout();
    protected Dialog formWindow;
    protected String formWindowWidth = "500px";

    protected Map<CrudOperation, String> windowCaptions = new HashMap<>();

    public WindowBasedCrudLayout() {
        getContent().setPadding(false);
        getContent().setMargin(false);
        getContent().add(mainLayout);

        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setPadding(false);
        mainLayout.setSpacing(true);
        setSizeFull();
        // FIXME find out Lumo styles
        // captionLabel.addStyleName(ValoTheme.LABEL_COLORED);
        // captionLabel.addStyleName(ValoTheme.LABEL_BOLD);
        // captionLabel.addStyleName(ValoTheme.LABEL_H3);
        // captionLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        captionLabel.setVisible(false);

        headerLayout.setVisible(false);
        headerLayout.setSpacing(true);

        toolbarLayout.setVisible(false);
        // FIXME find out Lumo style equivalent
        // toolbarLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        headerLayout.add(toolbarLayout);

        filterLayout.setVisible(false);
        filterLayout.setSpacing(true);
        headerLayout.add(filterLayout);

        Icon icon = VaadinIcon.SEARCH.create();
        filterLayout.add(icon);

        mainComponentLayout.setSizeFull();
        mainComponentLayout.setMargin(false);
        mainComponentLayout.setPadding(false);
        mainLayout.add(mainComponentLayout);
        mainLayout.expand(mainComponentLayout);

        setWindowCaption(CrudOperation.ADD, "Add");
        setWindowCaption(CrudOperation.UPDATE, "Update");
        setWindowCaption(CrudOperation.DELETE, "Are you sure you want to delete this item?");
    }

    @Override
    public void setCaption(String caption) {
        if (!captionLabel.isVisible()) {
            captionLabel.setVisible(true);
            mainLayout.getElement().insertChild(0, captionLabel.getElement());
        }

        captionLabel.setText(caption);
    }

    @Override
    public void setMainComponent(Component component) {
        mainComponentLayout.removeAll();
        mainComponentLayout.add(component);
    }

    @Override
    public void addFilterComponent(Component component) {
        if (!headerLayout.isVisible()) {
            headerLayout.setVisible(true);
            mainLayout.getElement().insertChild(mainLayout.getComponentCount() - 1, headerLayout.getElement());
        }

        filterLayout.setVisible(true);
        filterLayout.add(component);
    }

    @Override
    public void addToolbarComponent(Component component) {
        if (!headerLayout.isVisible()) {
            headerLayout.setVisible(true);
            mainLayout.getElement().insertChild(mainLayout.getComponentCount() - 1, headerLayout.getElement());
        }

        toolbarLayout.setVisible(true);
        toolbarLayout.add(component);
    }

    private void showWindow(String caption, Component form) {
        VerticalLayout windowLayout = new VerticalLayout(form);
        windowLayout.setWidth("100%");
        windowLayout.setMargin(false);
        windowLayout.setPadding(false);

        formWindow = new Dialog(new H3(caption), windowLayout);
        formWindow.setWidth(formWindowWidth);
        formWindow.open();
    }

    @Override
    public void showForm(CrudOperation operation, Component form) {
        if (!operation.equals(CrudOperation.READ)) {
            showWindow(windowCaptions.get(operation), form);
        }
    }

    @Override
    public void hideForm() {
        if (formWindow != null) {
            formWindow.close();
        }
    }

    public void setWindowCaption(CrudOperation operation, String caption) {
        windowCaptions.put(operation, caption);
    }

    public void setFormWindowWidth(String formWindowWidth) {
        this.formWindowWidth = formWindowWidth;
    }

}
