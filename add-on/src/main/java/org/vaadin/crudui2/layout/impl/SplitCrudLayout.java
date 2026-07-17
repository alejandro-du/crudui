package org.vaadin.crudui2.layout.impl;

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
	private VerticalLayout formContentContainer = new VerticalLayout();
	private VerticalLayout formContentHost = new VerticalLayout();
	private HorizontalLayout formHeader = new HorizontalLayout();
	private H3 crudActionCaption = new H3();
	private Scroller crudFormContainer = new Scroller(ScrollDirection.VERTICAL);
	private HorizontalLayout formActionContainer = new HorizontalLayout();
	private HorizontalLayout formActionButtonsContainer = new HorizontalLayout();
	private CrudList<B> currentCrudList;

	public SplitCrudLayout(Orientation orientation) {
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

		formContentHost.setPadding(false);
		formContentHost.setSpacing(false);
		formContentHost.setWidthFull();
		formContentContainer.add(formContentHost);

		crudFormContainer.setContent(formContentContainer);

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
	public void setCrudList(CrudList<B> crudList) {
		currentCrudList = crudList;
		crudListContainer.removeAll();
		crudListContainer.add((Component) crudList);
	}

	@Override
	public CrudList<B> getCrudList() {
		return currentCrudList;
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
}
