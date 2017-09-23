package org.vaadin.crudui.layout.impl;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.layout.CrudLayout;

import java.util.HashMap;
import java.util.Map;

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
    protected VerticalLayout formCaptionLayout = new VerticalLayout();

    protected Map<CrudOperation, String> formCaptions = new HashMap<>();

    public HorizontalSplitCrudLayout() {
        setCompositionRoot(mainLayout);
        mainLayout.setSizeFull();
        mainLayout.setSplitPosition(60, Unit.PERCENTAGE);
        mainLayout.setFirstComponent(leftLayout);
        mainLayout.setSecondComponent(rightLayout);
        setSizeFull();

        leftLayout.setSizeFull();
        leftLayout.setMargin(false);
        leftLayout.setSpacing(true);

        rightLayout.setWidth("100%");
        rightLayout.setMargin(false);
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
        mainComponentLayout.setMargin(false);
        leftLayout.addComponent(mainComponentLayout);
        leftLayout.setExpandRatio(mainComponentLayout, 1);

        formCaptionLayout.setMargin(new MarginInfo(false, true, false, true));

        formComponentLayout.setSizeFull();
        formComponentLayout.setMargin(false);
        rightLayout.addComponent(formComponentLayout);
        rightLayout.setExpandRatio(formComponentLayout, 1);

        setFormCaption(CrudOperation.DELETE, "Are you sure you want to delete this item?");
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

    @Override
    public void showForm(CrudOperation operation, Component form) {
        String caption = formCaptions.get(operation);
        if (caption != null) {
            Label label = new Label(caption);
            label.addStyleName(ValoTheme.LABEL_COLORED);
            formCaptionLayout.removeAllComponents();
            formCaptionLayout.addComponent(label);
            rightLayout.addComponent(formCaptionLayout, 1);
        } else {
            rightLayout.removeComponent(formCaptionLayout);
        }

        formComponentLayout.removeAllComponents();
        formComponentLayout.addComponent(form);
    }

    @Override
    public void hideForm() {
        formComponentLayout.removeAllComponents();
        rightLayout.removeComponent(formCaptionLayout);
    }

    public void setFormCaption(CrudOperation operation, String caption) {
        formCaptions.put(operation, caption);
    }

}
