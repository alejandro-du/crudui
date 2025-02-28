package org.vaadin.crudui.layout.impl;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.layout.CrudLayout;

/**
 * @author Alejandro Duarte.
 */
public abstract class AbstractTwoComponentsCrudLayout extends Composite<Div> implements CrudLayout, HasSize {

	protected VerticalLayout firstComponent = new VerticalLayout();
	protected VerticalLayout secondComponent = new VerticalLayout();
	protected HorizontalLayout firstComponentHeaderLayout = new HorizontalLayout();
	protected HorizontalLayout secondComponentHeaderLayout = new HorizontalLayout();
	protected VerticalLayout formComponentLayout = new VerticalLayout();
	protected HorizontalLayout formCaptionLayout = new HorizontalLayout();
	protected HorizontalLayout toolbar = new HorizontalLayout();
	protected HorizontalLayout filterLayout = new HorizontalLayout();
	protected VerticalLayout mainComponentLayout = new VerticalLayout();

	protected Map<CrudOperation, String> formCaptions = new HashMap<>();

	public AbstractTwoComponentsCrudLayout() {
		firstComponent.setMargin(false);
		firstComponent.setPadding(false);
		firstComponent.setSpacing(false);
		firstComponent.add(firstComponentHeaderLayout);

		firstComponentHeaderLayout.setVisible(false);
		firstComponentHeaderLayout.setSpacing(true);
		firstComponentHeaderLayout.setMargin(false);

		formCaptionLayout.setWidthFull();
		formCaptionLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

		secondComponent.setSizeFull();
		secondComponent.setMargin(false);
		secondComponent.setPadding(false);
		secondComponent.add(secondComponentHeaderLayout);

		secondComponentHeaderLayout.setVisible(false);
		secondComponentHeaderLayout.setWidthFull();
		secondComponentHeaderLayout.setSpacing(true);
		secondComponentHeaderLayout.setPadding(true);
		secondComponentHeaderLayout.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
		secondComponentHeaderLayout.add(formCaptionLayout, toolbar);

		formComponentLayout.setSizeFull();
		formComponentLayout.setMargin(false);
		formComponentLayout.setPadding(false);
		secondComponent.add(formComponentLayout);
		secondComponent.expand(formComponentLayout);

		toolbar.setJustifyContentMode(JustifyContentMode.END);

		filterLayout.setVisible(false);
		filterLayout.setSpacing(true);
		firstComponentHeaderLayout.add(filterLayout);

		mainComponentLayout.setSizeFull();
		mainComponentLayout.setMargin(false);
		mainComponentLayout.setPadding(false);
		firstComponent.add(mainComponentLayout);
		firstComponent.expand(mainComponentLayout);

		setFormCaption(CrudOperation.ADD, "Add");
		setFormCaption(CrudOperation.UPDATE, "Update");
		setFormCaption(CrudOperation.DELETE, "Are you sure you want to delete this item?");

		Component mainLayout = buildMainLayout();
		getContent().add(mainLayout);
		setSizeFull();
	}

	protected abstract Component buildMainLayout();

	@Override
	public void setMainComponent(Component component) {
		mainComponentLayout.removeAll();
		mainComponentLayout.add(component);
	}

	@Override
	public void addToolbarComponent(Component component) {
		secondComponentHeaderLayout.setVisible(true);
		toolbar.setVisible(true);
		toolbar.add(component);
	}

	@Override
	public void addFilterComponent(Component component) {
		if (!firstComponentHeaderLayout.isVisible()) {
			firstComponentHeaderLayout.setVisible(true);
			firstComponent.getElement().insertChild(firstComponent.getComponentCount() - 1,
					firstComponentHeaderLayout.getElement());
		}

		filterLayout.setVisible(true);
		filterLayout.add(component);
	}

	@Override
	public void showForm(CrudOperation operation, Component form, String formCaption) {
		String caption = (formCaption != null ? formCaption : formCaptions.get(operation));
		formCaptionLayout.removeAll();

		if (caption != null) {
			H3 h3 = new H3(caption);
			h3.setWidthFull();
			formCaptionLayout.add(h3);
		}

		formComponentLayout.removeAll();
		formComponentLayout.add(form);
	}

	@Override
	public void hideForm() {
		formComponentLayout.removeAll();
		formCaptionLayout.removeAll();
	}

	public void setFormCaption(CrudOperation operation, String caption) {
		formCaptions.put(operation, caption);
	}

}
