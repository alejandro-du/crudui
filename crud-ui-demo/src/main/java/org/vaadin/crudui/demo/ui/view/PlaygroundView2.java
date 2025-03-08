package org.vaadin.crudui.demo.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.UserService;

@Route("playground2")
@JsModule("theme-handler.js")
public class PlaygroundView2 extends VerticalLayout {

	public PlaygroundView2(UserService userService) {
		var grid = new Grid<>(User.class);
		grid.setItems(userService.findAll());



		add(grid);
		UI.getCurrent().getPage().executeJs("window.applySystemTheme()");
	}

}
