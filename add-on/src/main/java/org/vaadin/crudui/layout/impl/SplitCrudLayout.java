package org.vaadin.crudui.layout.impl;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;

import org.vaadin.crudui.form.CrudForm;
import org.vaadin.crudui.layout.CrudLayout;
import org.vaadin.crudui.layout.CrudLayoutFactory;
import org.vaadin.crudui.list.CrudList;

import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;

import java.util.ArrayList;
import java.util.List;

public class SplitCrudLayout<B> extends Composite<VerticalLayout> implements CrudLayout<B> {

	private HorizontalLayout headerLayout = new HorizontalLayout();
	private SplitLayout splitLayout = new SplitLayout();
	private HorizontalLayout filterContainer = new HorizontalLayout();
	private HorizontalLayout crudActionContainer = new HorizontalLayout();
	private VerticalLayout crudListContainer = new VerticalLayout();
	private VerticalLayout formLayout = new VerticalLayout();
	private VerticalLayout formContentContainer = new VerticalLayout();
	private VerticalLayout formContentHost = new VerticalLayout();
	private HorizontalLayout formHeader = new HorizontalLayout();
	private H3 crudActionCaption = new H3();
	private Scroller crudFormContainer = new Scroller(ScrollDirection.VERTICAL);
	private HorizontalLayout formActionContainer = new HorizontalLayout();
	private HorizontalLayout formActionButtonsContainer = new HorizontalLayout();

	/**
	 * Creates a new fluent builder for SplitCrudLayout.
	 *
	 * @param beanType The bean class (used for generic type inference)
	 * @param <B>      The bean type
	 * @return A builder for configuring the layout
	 */
	public static <B> Builder<B> of(Class<B> beanType) {
		return new Builder<>();
	}

	private SplitCrudLayout(Orientation orientation) {
		filterContainer.setWidthFull();

		headerLayout.add(filterContainer);
		headerLayout.setWidthFull();

		crudListContainer.setPadding(false);

		crudActionCaption.setVisible(false);

		crudActionContainer.setWidthFull();
		crudActionContainer.setJustifyContentMode(JustifyContentMode.END);

		formHeader.add(crudActionCaption, crudActionContainer);
		formHeader.expand(crudActionContainer);
		formHeader.setWidthFull();
		formHeader.setPadding(true);
		formHeader.setSpacing(true);
		formHeader.setDefaultVerticalComponentAlignment(Alignment.CENTER);

		formContentContainer.setPadding(true);
		formContentContainer.setSpacing(false);
		formContentContainer.setWidthFull();
		formContentContainer.setHeightFull();

		formContentHost.setPadding(false);
		formContentHost.setSpacing(false);
		formContentHost.setWidthFull();
		formContentContainer.add(formContentHost);

		crudFormContainer.setContent(formContentContainer);
		crudFormContainer.setWidthFull();  // Make scroller expand to fill width
		crudFormContainer.setHeightFull(); // Make scroller expand to fill height

		formActionContainer.setWidthFull();
		formActionContainer.setPadding(true);
		formActionContainer.setSpacing(true);
		formActionContainer.setVisible(false);

		formActionButtonsContainer.setSpacing(true);
		formActionButtonsContainer.setPadding(false);
		formActionButtonsContainer.setJustifyContentMode(JustifyContentMode.END);
        formActionButtonsContainer.setWidthFull();

		formActionContainer.add(formActionButtonsContainer);
		formActionContainer.expand(formActionButtonsContainer);

		formLayout.add(formHeader, crudFormContainer, formActionContainer);
		formLayout.setPadding(false);
		formLayout.setSizeFull();  // IMPORTANT: Make formLayout take full size of secondary panel
		formLayout.setFlexGrow(1, crudFormContainer);  // Make the scroller expand to fill available space

		splitLayout.addToPrimary(crudListContainer);
		splitLayout.addToSecondary(formLayout);
		splitLayout.setOrientation(orientation);
		splitLayout.setSplitterPosition(75);
		splitLayout.setSizeFull();
		splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);

		getContent().add(headerLayout, splitLayout);
		getContent().setSizeFull();
		getContent().setPadding(false);
	}

	@Override
	public void setCrudList(CrudList<B> crudList) {
		crudListContainer.removeAll();
		crudListContainer.add((Component) crudList);
	}

	@Override
	public void showCrudForm(CrudForm<B> crudForm) {
		formContentHost.removeAll();
		formContentHost.add((Component) crudForm);
		formActionContainer.setVisible(true);
	}

	@Override
	public void hideForm() {
		formContentHost.removeAll();
		formActionButtonsContainer.removeAll();
		setFormActionCaption(null);
		formActionContainer.setVisible(false);
	}

	@Override
	public void addCrudActionComponent(Component component) {
		crudActionContainer.add(component);
	}

	@Override
	public void addFormActionComponent(Component component) {
		formActionButtonsContainer.add(component);
	}

	@Override
	public void setFormActionCaption(String caption) {
		if (caption == null || caption.isBlank()) {
			crudActionCaption.setText("");
			crudActionCaption.setVisible(false);
			return;
		}

		crudActionCaption.setText(caption);
		crudActionCaption.setVisible(true);
	}

	@Override
	public void addFilterComponent(Component component) {
		filterContainer.add(component);
		filterContainer.expand(component);
	}

	/**
	 * Fluent builder for SplitCrudLayout.
	 *
	 * @param <B> The bean type
	 */
	public static class Builder<B> implements CrudLayoutFactory<B> {
		private Orientation orientation = Orientation.HORIZONTAL;
		private CrudList<B> crudList;
		private final List<Component> crudActionComponents = new ArrayList<>();
		private final List<Component> formActionComponents = new ArrayList<>();
		private final List<Component> filterComponents = new ArrayList<>();
		private String formActionCaption;

		Builder() {
		}

		/**
		 * Sets the orientation of the split layout.
		 *
		 * @param orientation The orientation (HORIZONTAL or VERTICAL)
		 * @return This builder for chaining
		 */
		public Builder<B> orientation(Orientation orientation) {
			this.orientation = orientation;
			return this;
		}

		/**
		 * Sets the CRUD list component.
		 *
		 * @param crudList The CRUD list
		 * @return This builder for chaining
		 */
		public Builder<B> crudList(CrudList<B> crudList) {
			this.crudList = crudList;
			return this;
		}

		/**
		 * Adds a CRUD action component (e.g., Create button).
		 *
		 * @param component The component to add
		 * @return This builder for chaining
		 */
		public Builder<B> addCrudActionComponent(Component component) {
			this.crudActionComponents.add(component);
			return this;
		}

		/**
		 * Adds a form action component (e.g., Save button).
		 *
		 * @param component The component to add
		 * @return This builder for chaining
		 */
		public Builder<B> addFormActionComponent(Component component) {
			this.formActionComponents.add(component);
			return this;
		}

		/**
		 * Adds a filter component.
		 *
		 * @param component The component to add
		 * @return This builder for chaining
		 */
		public Builder<B> addFilterComponent(Component component) {
			this.filterComponents.add(component);
			return this;
		}

		/**
		 * Sets the form action caption.
		 *
		 * @param caption The caption
		 * @return This builder for chaining
		 */
		public Builder<B> formActionCaption(String caption) {
			this.formActionCaption = caption;
			return this;
		}

		/**
		 * Builds and returns the SplitCrudLayout.
		 *
		 * @return The configured SplitCrudLayout
		 */
		public SplitCrudLayout<B> build() {
			SplitCrudLayout<B> layout = new SplitCrudLayout<>(orientation);

			if (crudList != null) {
				layout.setCrudList(crudList);
			}
			for (Component component : crudActionComponents) {
				layout.addCrudActionComponent(component);
			}
			for (Component component : formActionComponents) {
				layout.addFormActionComponent(component);
			}
			for (Component component : filterComponents) {
				layout.addFilterComponent(component);
			}
			if (formActionCaption != null) {
				layout.setFormActionCaption(formActionCaption);
			}

			return layout;
		}
	}
}
