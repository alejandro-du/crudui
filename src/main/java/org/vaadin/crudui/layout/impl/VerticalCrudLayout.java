package org.vaadin.crudui.layout.impl;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * @author Alejandro Duarte.
 */
public class VerticalCrudLayout extends AbstractTwoComponentsCrudLayout {

    @Override
    protected Component getMainLayout() {
        VerticalLayout mainLayout = new VerticalLayout(firstComponent, secondComponent);
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        firstComponent.setSizeFull();
        secondComponent.setSizeFull();
        return mainLayout;
    }

    @Override
    protected void addToolbarLayout(Div toolbarLayout) {
        firstComponentHeaderLayout.add(toolbarLayout);
    }

    @Override
    public void addToolbarComponent(Component component) {
        if (!firstComponentHeaderLayout.isVisible()) {
            firstComponentHeaderLayout.setVisible(true);
            firstComponent.getElement().insertChild(firstComponent.getComponentCount() - 1, firstComponentHeaderLayout.getElement());
        }

        toolbarLayout.setVisible(true);
        toolbarLayout.add(component);
    }

}
