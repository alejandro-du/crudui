package org.vaadin.crudui.layout.impl;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.splitlayout.SplitLayout;

/**
 * @author Alejandro Duarte.
 */
public class HorizontalSplitCrudLayout extends AbstractTwoComponentsCrudLayout {

    public HorizontalSplitCrudLayout() {
        secondComponentHeaderLayout.setMargin(false);
    }

    @Override
    protected SplitLayout buildMainLayout() {
        SplitLayout mainLayout = new SplitLayout(firstComponent, secondComponent);
        mainLayout.setSizeFull();
        mainLayout.setSplitterPosition(65);
        return mainLayout;
    }

    @Override
    protected void addToolbarLayout(Component toolbarLayout) {
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
