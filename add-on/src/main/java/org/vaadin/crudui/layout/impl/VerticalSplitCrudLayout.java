package org.vaadin.crudui.layout.impl;

import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;

/**
 * @author Alejandro Duarte.
 */
public class VerticalSplitCrudLayout extends AbstractTwoComponentsCrudLayout {

	@Override
	protected SplitLayout buildMainLayout() {
		SplitLayout mainLayout = new SplitLayout(firstComponent, secondComponent);
		mainLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
		mainLayout.setSizeFull();
		mainLayout.setSplitterPosition(65);
		mainLayout.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
		return mainLayout;
	}

}
