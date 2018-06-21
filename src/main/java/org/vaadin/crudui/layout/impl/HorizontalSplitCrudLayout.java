package org.vaadin.crudui.layout.impl;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;

/**
 * @author Alejandro Duarte.
 */
public class HorizontalSplitCrudLayout extends AbstractTwoComponentsCrudLayout {

    public HorizontalSplitCrudLayout() {
        secondComponentHeaderLayout.setMargin(false);
    }

    @Override
    protected SplitLayout getMainLayout() {
        SplitLayout mainLayout = new SplitLayout(firstComponent, secondComponent);
        mainLayout.setOrientation(Orientation.HORIZONTAL);
        mainLayout.setSizeFull();
        mainLayout.setSplitterPosition(60);
        return mainLayout;
    }

    @Override
    protected void addToolbarLayout(Div toolbarLayout) {
        secondComponentHeaderLayout.add(toolbarLayout);
    }

    @Override
    public void addToolbarComponent(Component component) {
        if (!secondComponentHeaderLayout.isVisible()) {
            secondComponentHeaderLayout.setVisible(true);
            secondComponent.getElement().insertChild(secondComponent.getComponentCount() - 1, secondComponentHeaderLayout.getElement());
        }

        toolbarLayout.setVisible(true);
        toolbarLayout.add(component);
    }

}
