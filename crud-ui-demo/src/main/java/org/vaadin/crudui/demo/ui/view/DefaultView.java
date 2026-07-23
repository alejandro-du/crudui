package org.vaadin.crudui.demo.ui.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.Crud;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui.demo.ui.MainLayout;

@Route(value = "default", layout = MainLayout.class)
public class DefaultView extends VerticalLayout {

	public DefaultView(UserService userService) {
        var crud = Crud.of(User.class)
                .onCreate(userService::save)
                .onRead(userService::findAll)
                .onUpdate(userService::save)
                .onDelete(userService::delete)
                .build();

        add(crud);
		setSizeFull();
	}

}
