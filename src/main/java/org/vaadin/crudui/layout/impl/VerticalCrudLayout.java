package org.vaadin.crudui.layout.impl;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Alejandro Duarte.
 */
public class VerticalCrudLayout extends AbstractTwoComponentsCrudLayout {

    @Override
    protected AbstractComponentContainer getMainLayout() {
        VerticalLayout mainLayout = new VerticalLayout(firstComponent, secondComponent);
        mainLayout.setMargin(false);
        return mainLayout;
    }

    @Override
    protected void addToolbarLayout(CssLayout toolbarLayout) {
        firstComponentHeaderLayout.addComponent(toolbarLayout);
    }

    @Override
    public void addToolbarComponent(Component component) {
        if (!firstComponentHeaderLayout.isVisible()) {
            firstComponentHeaderLayout.setVisible(true);
            firstComponent.addComponent(firstComponentHeaderLayout, firstComponent.getComponentCount() - 1);
        }

        toolbarLayout.setVisible(true);
        toolbarLayout.addComponent(component);
    }

}
