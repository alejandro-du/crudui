package org.vaadin.crudui.layout.impl;

import com.vaadin.icons.VaadinIcons;
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
public abstract class AbstractTwoComponentsCrudLayout extends Composite implements CrudLayout {

    protected VerticalLayout firstComponent = new VerticalLayout();
    protected VerticalLayout secondComponent = new VerticalLayout();
    protected Label captionLabel = new Label();
    protected HorizontalLayout firstComponentHeaderLayout = new HorizontalLayout();
    protected HorizontalLayout secondComponentHeaderLayout = new HorizontalLayout();
    protected CssLayout toolbarLayout = new CssLayout();
    protected HorizontalLayout filterLayout = new HorizontalLayout();
    protected VerticalLayout mainComponentLayout = new VerticalLayout();
    protected VerticalLayout formComponentLayout = new VerticalLayout();
    protected VerticalLayout formCaptionLayout = new VerticalLayout();

    protected Map<CrudOperation, String> formCaptions = new HashMap<>();

    public AbstractTwoComponentsCrudLayout() {
        AbstractComponentContainer mainLayout = getMainLayout();
        setCompositionRoot(mainLayout);
        setSizeFull();

        firstComponent.setSizeFull();
        firstComponent.setMargin(false);
        firstComponent.setSpacing(true);

        secondComponent.setWidth("100%");
        secondComponent.setMargin(false);
        secondComponent.setSpacing(true);

        captionLabel.addStyleName(ValoTheme.LABEL_COLORED);
        captionLabel.addStyleName(ValoTheme.LABEL_BOLD);
        captionLabel.addStyleName(ValoTheme.LABEL_H3);
        captionLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        captionLabel.setVisible(false);

        firstComponentHeaderLayout.setVisible(false);
        firstComponentHeaderLayout.setSpacing(true);

        secondComponentHeaderLayout.setVisible(false);
        secondComponentHeaderLayout.setSpacing(true);

        toolbarLayout.setVisible(false);
        toolbarLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        addToolbarLayout(toolbarLayout);

        filterLayout.setVisible(false);
        filterLayout.setSpacing(true);
        firstComponentHeaderLayout.addComponent(filterLayout);

        Label filterIcon = new Label();
        filterIcon.setIcon(VaadinIcons.SEARCH);
        filterLayout.addComponent(filterIcon);

        mainComponentLayout.setSizeFull();
        mainComponentLayout.setMargin(false);
        firstComponent.addComponent(mainComponentLayout);
        firstComponent.setExpandRatio(mainComponentLayout, 1);

        formCaptionLayout.setMargin(new MarginInfo(false, true, false, true));

        formComponentLayout.setSizeFull();
        formComponentLayout.setMargin(false);
        secondComponent.addComponent(formComponentLayout);
        secondComponent.setExpandRatio(formComponentLayout, 1);

        setFormCaption(CrudOperation.DELETE, "Are you sure you want to delete this item?");
    }

    protected abstract AbstractComponentContainer getMainLayout();

    protected abstract void addToolbarLayout(CssLayout toolbarLayout);

    @Override
    public void setCaption(String caption) {
        if (!captionLabel.isVisible()) {
            captionLabel.setVisible(true);
            firstComponent.addComponent(captionLabel, 0);
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
        if (!firstComponentHeaderLayout.isVisible()) {
            firstComponentHeaderLayout.setVisible(true);
            firstComponent.addComponent(firstComponentHeaderLayout, firstComponent.getComponentCount() - 1);
        }

        filterLayout.setVisible(true);
        filterLayout.addComponent(component);
    }

    @Override
    public void showForm(CrudOperation operation, Component form) {
        String caption = formCaptions.get(operation);
        if (caption != null) {
            Label label = new Label(caption);
            label.addStyleName(ValoTheme.LABEL_COLORED);
            formCaptionLayout.removeAllComponents();
            formCaptionLayout.addComponent(label);
            secondComponent.addComponent(formCaptionLayout, secondComponent.getComponentCount() - 1);
        } else {
            secondComponent.removeComponent(formCaptionLayout);
        }

        formComponentLayout.removeAllComponents();
        formComponentLayout.addComponent(form);
    }

    @Override
    public void hideForm() {
        formComponentLayout.removeAllComponents();
        secondComponent.removeComponent(formCaptionLayout);
    }

    public void setFormCaption(CrudOperation operation, String caption) {
        formCaptions.put(operation, caption);
    }

}
