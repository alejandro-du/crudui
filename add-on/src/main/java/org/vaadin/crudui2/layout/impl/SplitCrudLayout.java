package org.vaadin.crudui2.layout.impl;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;

import org.vaadin.crudui2.form.CrudForm;
import org.vaadin.crudui2.layout.CrudLayout;
import org.vaadin.crudui2.list.CrudList;

public class SplitCrudLayout<B> extends Composite<VerticalLayout> implements CrudLayout<B> {

	private HorizontalLayout headerLayout = new HorizontalLayout();
	private SplitLayout splitLayout = new SplitLayout();
	private HorizontalLayout filterContainer = new HorizontalLayout();
	private HorizontalLayout crudActionContainer = new HorizontalLayout();
	private VerticalLayout crudListContainer = new VerticalLayout();
	private VerticalLayout formLayout = new VerticalLayout();
	private HorizontalLayout formHeader = new HorizontalLayout();
	private Scroller crudFormContainer = new Scroller(ScrollDirection.VERTICAL);
	private HorizontalLayout formActionContainer = new HorizontalLayout();

	public SplitCrudLayout(Orientation orientation) {
		filterContainer.setWidthFull();

		headerLayout.add(filterContainer);
		headerLayout.setWidthFull();

		crudListContainer.setPadding(false);

		formHeader.add(crudActionContainer);
		formHeader.setWidthFull();
		formHeader.setPadding(true);
		formHeader.getStyle().set("background-color", "var(--lumo-contrast-5pct)");

		crudFormContainer.getStyle().set("padding-left", "var(--lumo-space-m)");

		formActionContainer.setWidthFull();
		formActionContainer.setPadding(true);
		formActionContainer.setJustifyContentMode(JustifyContentMode.END);
		formActionContainer.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
		formActionContainer.setVisible(false);

		formLayout.add(formHeader, crudFormContainer, formActionContainer);
		formLayout.setPadding(false);

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

	public SplitCrudLayout() {
		this(Orientation.HORIZONTAL);
	}

	@Override
	public void setCrudList(CrudList crudList) {
		crudListContainer.removeAll();
		crudListContainer.add((Component) crudList);
	}

	@Override
	public CrudList<B> getCrudList() {
		return (CrudList<B>) crudListContainer.getComponentAt(0);
	}

	@Override
	public void showCrudForm(CrudForm crudForm) {
		crudFormContainer.setContent((Component) crudForm);
		formActionContainer.setVisible(true);
	}

	@Override
	public void hideForm() {
		crudFormContainer.setContent(null);
		formActionContainer.removeAll();
		formActionContainer.setVisible(false);
	}

	@Override
	public void addCrudActionComponent(Component component) {
		crudActionContainer.add(component);
	}

	@Override
	public void addFormActionComponent(Component component) {
		formActionContainer.add(component);
	}

	@Override
	public void addFilterComponent(Component component) {
		filterContainer.add(component);
		filterContainer.expand(component);
	}
}
