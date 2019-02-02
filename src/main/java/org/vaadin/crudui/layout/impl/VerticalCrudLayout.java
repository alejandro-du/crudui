package org.vaadin.crudui.layout.impl;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * @author Alejandro Duarte.
 */
public class VerticalCrudLayout extends AbstractTwoComponentsCrudLayout {

    public VerticalCrudLayout() {
        filterLayout.setPadding(false);
        filterLayout.setMargin(false);
        firstComponentHeaderLayout.setMargin(true);
        secondComponent.setMargin(false);
    }

    @Override
    protected Component buildMainLayout() {
        VerticalLayout mainLayout = new VerticalLayout(firstComponent, secondComponent);
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setPadding(false);
        firstComponent.setSizeFull();
        secondComponent.setSizeFull();
        return mainLayout;
    }

    @Override
    protected void addToolbarLayout(Component toolbarLayout) {
        firstComponentHeaderLayout.add(toolbarLayout);
    }

    @Override
    public void addToolbarComponent(Component component) {
        if (!firstComponentHeaderLayout.isVisible()) {
            firstComponentHeaderLayout.setVisible(true);
            firstComponent.getElement()
                    .insertChild(firstComponent.getComponentCount() - 1, firstComponentHeaderLayout.getElement());
        }

        toolbarLayout.setVisible(true);
        toolbarLayout.add(component);
    }

}
