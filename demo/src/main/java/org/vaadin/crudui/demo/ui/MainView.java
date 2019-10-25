package org.vaadin.crudui.demo.ui;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;

@PWA(name = "Crud UI add-on Demo", shortName = "Crud UI demo")
@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        add(
                new H1("Crud UI add-on demo"),
                new Html("<span>" +
                        "This is the demo app for the" +
                        " <a href='https://vaadin.com/directory/component/crud-ui-add-on'>Crud UI add-on for Vaadin</a>." +
                        " The full source code for this demo application is" +
                        " <a href='https://github.com/alejandro-du/crudui/tree/master/demo'>available on GitHub</a>." +
                        " You can find a link to the source code of each specific demo view at the bottom of each view." +
                        "</span>"),
                new H2("Demo views"),
                new RouterLink("Basic CRUD ", BasicCrud.class),
                new RouterLink("Basic CRUD with filter", BasicCrudWithFilter.class),
                new RouterLink("CRUD with lazy loading", CrudWithLazyLoading.class)
        );
    }

}
