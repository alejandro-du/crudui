package org.vaadin.crudui.demo.ui.view;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.demo.ui.MainLayout;

/**
 * @author Alejandro Duarte
 */
@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

    public HomeView() {
        add(
                new H1("Crud UI add-on demo"),
                new Html("<span>" +
                        "This is the demo app for the" +
                        " <a href='https://vaadin.com/directory/component/crud-ui-add-on'>Crud UI add-on for Vaadin</a>." +
                        " The full source code for this demo application is" +
                        " <a href='https://github.com/alejandro-du/crudui/tree/master/demo'>available on GitHub</a>." +
                        " You can find a link to the source code of each specific demo view at the <b>bottom of each view</b>." +
                        "</span>")
        );
    }

}
