package org.vaadin.crudui.layout.impl;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
    protected HorizontalLayout firstComponentHeaderLayout = new HorizontalLayout();
    protected HorizontalLayout secondComponentHeaderLayout = new HorizontalLayout();
    protected HorizontalLayout toolbarLayout = new HorizontalLayout();
    protected HorizontalLayout filterLayout = new HorizontalLayout();
    protected VerticalLayout mainComponentLayout = new VerticalLayout();
    protected VerticalLayout formComponentLayout = new VerticalLayout();
    protected VerticalLayout formCaptionLayout = new VerticalLayout();

    protected Map<CrudOperation, String> formCaptions = new HashMap<>();

    public AbstractTwoComponentsCrudLayout() {
        Component mainLayout = buildMainLayout();
        getContent().add(mainLayout);
        setSizeFull();

        firstComponent.setMargin(false);
        firstComponent.setPadding(false);
        firstComponent.setSpacing(false);

        secondComponent.setMargin(true);
        secondComponent.setPadding(false);
        secondComponent.setSpacing(true);

        firstComponentHeaderLayout.setVisible(false);
        firstComponentHeaderLayout.setSpacing(true);
        firstComponentHeaderLayout.setMargin(false);

        secondComponentHeaderLayout.setVisible(false);
        secondComponentHeaderLayout.setSpacing(true);

        toolbarLayout.setVisible(false);
        // FIXME figure out replacement
        // toolbarLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        addToolbarLayout(toolbarLayout);

        filterLayout.setVisible(false);
        filterLayout.setSpacing(true);
        filterLayout.setMargin(true);
        firstComponentHeaderLayout.add(filterLayout);

        filterLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        Icon icon = VaadinIcon.SEARCH.create();
        icon.getStyle().set("padding-left", "1em");
        icon.setSize(".9em");
        filterLayout.add(icon);

        mainComponentLayout.setSizeFull();
        mainComponentLayout.setMargin(false);
        mainComponentLayout.setPadding(false);
        firstComponent.add(mainComponentLayout);
        firstComponent.expand(mainComponentLayout);

        formCaptionLayout.setMargin(true);

        formComponentLayout.setSizeFull();
        formComponentLayout.setMargin(false);
        formComponentLayout.setPadding(false);
        secondComponent.add(formComponentLayout);
        secondComponent.expand(formComponentLayout);

        setFormCaption(CrudOperation.DELETE, "Are you sure you want to delete this item?");
    }

    protected abstract Component buildMainLayout();

    protected abstract void addToolbarLayout(Component toolbarLayout);

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

//    @Override
//    public void showForm(CrudOperation operation, Component form) {
//        String caption = formCaptions.get(operation);
//        showDialog(caption, form);
//    }
    
    @Override
    public void showForm(CrudOperation operation, Component form, String formCaption) {
        String caption = (formCaption != null ? formCaption : formCaptions.get(operation));
        showDialog(caption, form);
    }

	@Override
	public void showDialog(String caption, Component form) {
		if (caption != null) {
            Div label = new Div(new Text(caption));
            label.getStyle().set("color", "var(--lumo-primary-text-color)");
            formCaptionLayout.removeAll();
            formCaptionLayout.add(label);
            secondComponent.addComponentAtIndex(secondComponent.getComponentCount() - 1, formCaptionLayout);
        } else if (formCaptionLayout.getElement().getParent() != null) {
            formCaptionLayout.getElement().getParent().removeChild(formCaptionLayout.getElement());
        }

        formComponentLayout.removeAll();
        formComponentLayout.add(form);
	}

	@Override
    public void hideForm() {
        formComponentLayout.removeAll();
        if (formCaptionLayout.getElement().getParent() != null) {
            formCaptionLayout.getElement().getParent().removeChild(formCaptionLayout.getElement());
        }
    }

    public void setFormCaption(CrudOperation operation, String caption) {
        formCaptions.put(operation, caption);
    }

}
