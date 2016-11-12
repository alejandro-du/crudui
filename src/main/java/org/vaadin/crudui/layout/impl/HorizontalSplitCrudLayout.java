package org.vaadin.crudui.layout.impl;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.crudui.layout.CrudLayout;

/**
 * @author Alejandro Duarte.
 */
public class HorizontalSplitCrudLayout extends CustomComponent implements CrudLayout {

    protected HorizontalSplitPanel mainLayout = new HorizontalSplitPanel();
    protected VerticalLayout leftLayout = new VerticalLayout();
    protected VerticalLayout rightLayout = new VerticalLayout();
    protected Label captionLabel = new Label();
    protected HorizontalLayout leftHeaderLayout = new HorizontalLayout();
    protected HorizontalLayout rightHeaderLayout = new HorizontalLayout();
    protected CssLayout toolbarLayout = new CssLayout();
    protected HorizontalLayout filterLayout = new HorizontalLayout();
    protected VerticalLayout mainComponentLayout = new VerticalLayout();
    protected VerticalLayout formComponentLayout = new VerticalLayout();

    public HorizontalSplitCrudLayout() {
        setCompositionRoot(mainLayout);
        mainLayout.setSizeFull();
        mainLayout.setSplitPosition(60, Unit.PERCENTAGE);
        mainLayout.setFirstComponent(leftLayout);
        mainLayout.setSecondComponent(rightLayout);
        setSizeFull();

        leftLayout.setSizeFull();
        leftLayout.setSpacing(true);

        rightLayout.setWidth("100%");
        rightLayout.setSpacing(true);

        captionLabel.addStyleName(ValoTheme.LABEL_COLORED);
        captionLabel.addStyleName(ValoTheme.LABEL_BOLD);
        captionLabel.addStyleName(ValoTheme.LABEL_H3);
        captionLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        captionLabel.setVisible(false);

        leftHeaderLayout.setVisible(false);
        leftHeaderLayout.setSpacing(true);

        rightHeaderLayout.setVisible(false);
        rightHeaderLayout.setSpacing(true);
        rightHeaderLayout.setMargin(new MarginInfo(false, false, false, true));

        toolbarLayout.setVisible(false);
        toolbarLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        rightHeaderLayout.addComponent(toolbarLayout);

        filterLayout.setVisible(false);
        filterLayout.setSpacing(true);
        leftHeaderLayout.addComponent(filterLayout);

        Label filterIcon = new Label();
        filterIcon.setIcon(FontAwesome.SEARCH);
        filterLayout.addComponent(filterIcon);

        mainComponentLayout.setSizeFull();
        leftLayout.addComponent(mainComponentLayout);
        leftLayout.setExpandRatio(mainComponentLayout, 1);

        formComponentLayout.setSizeFull();
        rightLayout.addComponent(formComponentLayout);
        rightLayout.setExpandRatio(formComponentLayout, 1);
    }

    @Override
    public void setCaption(String caption) {
        if (!captionLabel.isVisible()) {
            captionLabel.setVisible(true);
            leftLayout.addComponent(captionLabel, 0);
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
        if (!leftHeaderLayout.isVisible()) {
            leftHeaderLayout.setVisible(true);
            leftLayout.addComponent(leftHeaderLayout, leftLayout.getComponentCount() - 1);
        }

        filterLayout.setVisible(true);
        filterLayout.addComponent(component);
    }

    @Override
    public void addToolbarComponent(Component component) {
        if (!rightHeaderLayout.isVisible()) {
            rightHeaderLayout.setVisible(true);
            rightLayout.addComponent(rightHeaderLayout, rightLayout.getComponentCount() - 1);
        }

        toolbarLayout.setVisible(true);
        toolbarLayout.addComponent(component);
    }

    private void showForm(String caption, Component crudForm) {
        formComponentLayout.removeAllComponents();
        formComponentLayout.addComponent(crudForm);
    }

    @Override
    public void showReadForm(String caption, Component formComponent) {
        showForm(caption, formComponent);
    }

    @Override
    public void showAddForm(String caption, Component formComponent) {
        showForm(caption, formComponent);
    }

    @Override
    public void showUpdateForm(String caption, Component formComponent) {
        showForm(caption, formComponent);
    }

    @Override
    public void showDeleteForm(String caption, Component formComponent) {
        showForm(caption, formComponent);
    }

    @Override
    public void hideForm() {
        formComponentLayout.removeAllComponents();
    }

}
