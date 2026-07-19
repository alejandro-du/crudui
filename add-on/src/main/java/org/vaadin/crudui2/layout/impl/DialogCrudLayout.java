package org.vaadin.crudui2.layout.impl;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.crudui2.form.CrudForm;
import org.vaadin.crudui2.layout.CrudLayout;
import org.vaadin.crudui2.layout.CrudLayoutFactory;
import org.vaadin.crudui2.list.CrudList;

/**
 * A dialog-based implementation of CrudLayout that shows a CrudList with action
 * components on top, and displays forms in a modal dialog.
 *
 * Usage:
 * <pre>
 * Crud&lt;User&gt; crud = Crud.of(User.class)
 *     .layout(DialogCrudLayout.of(User.class).build())
 *     .build();
 * </pre>
 *
 * @param <B> The bean type
 */
public class DialogCrudLayout<B> extends Composite<VerticalLayout> implements CrudLayout<B> {

	private final HorizontalLayout headerLayout = new HorizontalLayout();
	private final HorizontalLayout filterContainer = new HorizontalLayout();
	private final HorizontalLayout crudActionContainer = new HorizontalLayout();
	private final VerticalLayout crudListContainer = new VerticalLayout();
	private final Dialog formDialog = new Dialog();
	private final VerticalLayout formLayout = new VerticalLayout();
	private final H3 crudActionCaption = new H3();
	private final HorizontalLayout formHeader = new HorizontalLayout();
	private final VerticalLayout formContentContainer = new VerticalLayout();
	private final VerticalLayout formContentHost = new VerticalLayout();
	private final Scroller crudFormContainer = new Scroller(ScrollDirection.VERTICAL);
	private final HorizontalLayout formActionContainer = new HorizontalLayout();
	private final HorizontalLayout formActionButtonsContainer = new HorizontalLayout();

	/**
	 * Creates a new fluent builder for DialogCrudLayout.
	 *
	 * @param beanType The bean class (used for generic type inference)
	 * @param <B>      The bean type
	 * @return A builder for configuring the layout
	 */
	public static <B> Builder<B> of(Class<B> beanType) {
		return new Builder<>();
	}

	private DialogCrudLayout() {
		// Configure filter container
		filterContainer.setWidthFull();

		// Configure CRUD action container
		crudActionCaption.setVisible(false);
		crudActionContainer.setWidthFull();
		crudActionContainer.setJustifyContentMode(JustifyContentMode.START);

		// Configure header with filter and CRUD actions
		headerLayout.add(crudActionContainer, filterContainer);
		headerLayout.expand(filterContainer);
		headerLayout.setWidthFull();
		headerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

		// Configure CRUD list container
		crudListContainer.setPadding(false);

		// Configure form header (inside dialog)
		formHeader.add(crudActionCaption);
		formHeader.setWidthFull();
		formHeader.setPadding(true);
		formHeader.setSpacing(true);
		formHeader.setDefaultVerticalComponentAlignment(Alignment.CENTER);

		// Configure form content
		formContentContainer.setPadding(true);
		formContentContainer.setSpacing(false);
		formContentContainer.setWidthFull();
		formContentHost.setPadding(false);
		formContentHost.setSpacing(false);
		formContentHost.setWidthFull();
		formContentContainer.add(formContentHost);

		// Configure form container scroller
		crudFormContainer.setContent(formContentContainer);

		// Configure form action container
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

		// Assemble form layout (dialog content)
		formLayout.add(formHeader, crudFormContainer, formActionContainer);
		formLayout.setPadding(false);

		// Configure the dialog
		formDialog.setModal(true);
		formDialog.setDraggable(true);
		formDialog.setResizable(true);
		formDialog.setWidth("50vw");
		formDialog.setHeight("auto");
		formDialog.setCloseOnEsc(true);
		formDialog.setCloseOnOutsideClick(false);
		formDialog.add(formLayout);

		// Assemble main layout
		getContent().add(headerLayout, crudListContainer);
		getContent().setSizeFull();
		getContent().setPadding(false);
	}

	@Override
	public void setCrudList(CrudList<B> crudList) {
		crudListContainer.removeAll();
		Component crudListComponent = (Component) crudList;
		crudListContainer.add(crudListComponent);
		crudListContainer.expand(crudListComponent);
	}

	@Override
	public void showCrudForm(CrudForm<B> crudForm) {
		formContentHost.removeAll();
		formContentHost.add((Component) crudForm);
		formActionContainer.setVisible(true);
		formDialog.open();
	}

	@Override
	public void hideForm() {
		formContentHost.removeAll();
		formActionButtonsContainer.removeAll();
		setFormActionCaption(null);
		formActionContainer.setVisible(false);
		formDialog.close();
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
	 * Fluent builder for DialogCrudLayout.
	 *
	 * @param <B> The bean type
	 */
	public static class Builder<B> implements CrudLayoutFactory<B> {
		private CrudList<B> crudList;
		private final List<Component> crudActionComponents = new ArrayList<>();
		private final List<Component> formActionComponents = new ArrayList<>();
		private final List<Component> filterComponents = new ArrayList<>();
		private String formActionCaption;
		private String dialogWidth = "50vw";
		private boolean dialogModal = true;
		private boolean dialogDraggable = true;
		private boolean dialogResizable = true;
		private boolean dialogCloseOnEsc = true;
		private boolean dialogCloseOnOutsideClick = false;

		Builder() {
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
		 * Sets the dialog width. Default is "50vw".
		 *
		 * @param width The width (e.g., "50vw", "600px")
		 * @return This builder for chaining
		 */
		public Builder<B> dialogWidth(String width) {
			this.dialogWidth = width;
			return this;
		}

		/**
		 * Sets whether the dialog is modal. Default is true.
		 *
		 * @param modal True to make it modal
		 * @return This builder for chaining
		 */
		public Builder<B> dialogModal(boolean modal) {
			this.dialogModal = modal;
			return this;
		}

		/**
		 * Sets whether the dialog is draggable. Default is true.
		 *
		 * @param draggable True to make it draggable
		 * @return This builder for chaining
		 */
		public Builder<B> dialogDraggable(boolean draggable) {
			this.dialogDraggable = draggable;
			return this;
		}

		/**
		 * Sets whether the dialog is resizable. Default is true.
		 *
		 * @param resizable True to make it resizable
		 * @return This builder for chaining
		 */
		public Builder<B> dialogResizable(boolean resizable) {
			this.dialogResizable = resizable;
			return this;
		}

		/**
		 * Sets whether the dialog closes on ESC key. Default is true.
		 *
		 * @param closeOnEsc True to close on ESC
		 * @return This builder for chaining
		 */
		public Builder<B> dialogCloseOnEsc(boolean closeOnEsc) {
			this.dialogCloseOnEsc = closeOnEsc;
			return this;
		}

		/**
		 * Sets whether the dialog closes when clicking outside. Default is false.
		 *
		 * @param closeOnOutsideClick True to close on outside click
		 * @return This builder for chaining
		 */
		public Builder<B> dialogCloseOnOutsideClick(boolean closeOnOutsideClick) {
			this.dialogCloseOnOutsideClick = closeOnOutsideClick;
			return this;
		}

		/**
		 * Builds and returns the DialogCrudLayout.
		 *
		 * @return The configured DialogCrudLayout
		 */
		public DialogCrudLayout<B> build() {
			DialogCrudLayout<B> layout = new DialogCrudLayout<>();

			// Configure dialog
			layout.formDialog.setModal(dialogModal);
			layout.formDialog.setDraggable(dialogDraggable);
			layout.formDialog.setResizable(dialogResizable);
			layout.formDialog.setWidth(dialogWidth);
			layout.formDialog.setCloseOnEsc(dialogCloseOnEsc);
			layout.formDialog.setCloseOnOutsideClick(dialogCloseOnOutsideClick);

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
