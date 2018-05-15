package org.vaadin.crudui.layout.impl;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcons;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.layout.CrudLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alejandro Duarte.
 */
public abstract class AbstractTwoComponentsCrudLayout extends Composite<Div> implements CrudLayout, HasSize {

    protected VerticalLayout firstComponent = new VerticalLayout();
    protected VerticalLayout secondComponent = new VerticalLayout();
    protected Div captionLabel = new Div();
    protected HorizontalLayout firstComponentHeaderLayout = new HorizontalLayout();
    protected HorizontalLayout secondComponentHeaderLayout = new HorizontalLayout();
    protected Div toolbarLayout = new Div();
    protected HorizontalLayout filterLayout = new HorizontalLayout();
    protected VerticalLayout mainComponentLayout = new VerticalLayout();
    protected VerticalLayout formComponentLayout = new VerticalLayout();
    protected VerticalLayout formCaptionLayout = new VerticalLayout();

    protected Map<CrudOperation, String> formCaptions = new HashMap<>();

    public AbstractTwoComponentsCrudLayout() {
        Component mainLayout = getMainLayout();
        getContent().add(mainLayout);
        setSizeFull();

        firstComponent.setSizeFull();
        firstComponent.setMargin(false);
        firstComponent.setSpacing(true);

        secondComponent.setWidth("100%");
        secondComponent.setMargin(false);
        secondComponent.setSpacing(true);

        // FIXME figure out replacement
        // captionLabel.addStyleName(ValoTheme.LABEL_COLORED);
        // captionLabel.addStyleName(ValoTheme.LABEL_BOLD);
        // captionLabel.addStyleName(ValoTheme.LABEL_H3);
        // captionLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        captionLabel.setVisible(false);

        firstComponentHeaderLayout.setVisible(false);
        firstComponentHeaderLayout.setSpacing(true);

        secondComponentHeaderLayout.setVisible(false);
        secondComponentHeaderLayout.setSpacing(true);

        toolbarLayout.setVisible(false);
        // FIXME figure out replacement
        // toolbarLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        addToolbarLayout(toolbarLayout);

        filterLayout.setVisible(false);
        filterLayout.setSpacing(true);
        firstComponentHeaderLayout.add(filterLayout);

        Icon filterIcon = new Icon(VaadinIcons.SEARCH);
        filterLayout.add(filterIcon);

        mainComponentLayout.setSizeFull();
        mainComponentLayout.setMargin(false);
        firstComponent.add(mainComponentLayout);
        firstComponent.setFlexGrow(1, mainComponentLayout);

        formCaptionLayout.setMargin(true);

        formComponentLayout.setSizeFull();
        formComponentLayout.setMargin(false);
        secondComponent.add(formComponentLayout);
        secondComponent.setFlexGrow(1, formComponentLayout);

        setFormCaption(CrudOperation.DELETE, "Are you sure you want to delete this item?");
    }

    protected abstract Component getMainLayout();

    protected abstract void addToolbarLayout(Div toolbarLayout);

    @Override
    public void setCaption(String caption) {
        if (!captionLabel.isVisible()) {
            captionLabel.setVisible(true);
            firstComponent.getElement().insertChild(0, captionLabel.getElement());
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
        if (!firstComponentHeaderLayout.isVisible()) {
            firstComponentHeaderLayout.setVisible(true);
            firstComponent.getElement().insertChild(firstComponent.getComponentCount() - 1, firstComponentHeaderLayout.getElement());
        }

        filterLayout.setVisible(true);
        filterLayout.add(component);
    }

    @Override
    public void showForm(CrudOperation operation, Component form) {
        String caption = formCaptions.get(operation);
        if (caption != null) {
            Div label = new Div(new Text(caption));
            // FIXME figure out replacement
            // label.addStyleName(ValoTheme.LABEL_COLORED);
            formCaptionLayout.removeAll();
            formCaptionLayout.add(label);
            secondComponent.getElement().insertChild(secondComponent.getComponentCount() - 1, formCaptionLayout.getElement());
        } else if (formCaptionLayout.getElement().getParent() != null) {
            secondComponent.getElement().removeChild(formCaptionLayout.getElement());
        }

        formComponentLayout.removeAll();
        formComponentLayout.add(form);
    }

    @Override
    public void hideForm() {
        formComponentLayout.removeAll();
        if (formCaptionLayout.getElement().getParent() != null) {
            secondComponent.getElement().removeChild(formCaptionLayout.getElement());
        }
    }

    public void setFormCaption(CrudOperation operation, String caption) {
        formCaptions.put(operation, caption);
    }

}
