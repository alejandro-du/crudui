package org.vaadin.crudui.demo.ui;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import org.vaadin.crudui.demo.DemoUtils;
import org.vaadin.crudui.demo.ui.view.CrudWithSplitLayoutView;
import org.vaadin.crudui.demo.ui.view.CustomCrudView;
import org.vaadin.crudui.demo.ui.view.CustomTreeCrudView;
import org.vaadin.crudui.demo.ui.view.DefaultCrudView;
import org.vaadin.crudui.demo.ui.view.HomeView;

public class MainLayout extends AppLayout implements BeforeEnterObserver, AfterNavigationObserver {

	private Tabs tabs = new Tabs();
	private Map<Tab, Class<? extends HasComponents>> tabToView = new HashMap<>();
	private Map<Class<? extends HasComponents>, Tab> viewToTab = new HashMap<>();

	public MainLayout() {
		Image img = new Image("https://i.imgur.com/GPpnszs.png", "Vaadin Logo");
		img.setHeight("44px");
		addToNavbar(img);

		tabs.addSelectedChangeListener(event -> tabsSelectionChanged(event));
		addToNavbar(tabs);

		addTab(HomeView.class);
		addTab(DefaultCrudView.class);
		addTab(CustomCrudView.class);
		addTab(CrudWithSplitLayoutView.class);
		addTab(CustomTreeCrudView.class);
	}

	private void tabsSelectionChanged(Tabs.SelectedChangeEvent event) {
		if (event.isFromClient()) {
			UI.getCurrent().navigate((Class<? extends Component>) tabToView.get(event.getSelectedTab()));
		}
	}

	private void addTab(Class<? extends HasComponents> clazz) {
		Tab tab = new Tab(DemoUtils.getViewName(clazz));
		tabs.add(tab);
		tabToView.put(tab, clazz);
		viewToTab.put(clazz, tab);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		selectTabByCurrentView(event);
	}

	public void selectTabByCurrentView(BeforeEnterEvent event) {
		Class<?> viewClass = event.getNavigationTarget();
		tabs.setSelectedTab(viewToTab.get(viewClass));
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		updatePageTitle();
		addSourceCodeAnchorToCurrentView();
	}

	public void updatePageTitle() {
		Class<? extends HasComponents> viewClass = tabToView.get(tabs.getSelectedTab());
		UI.getCurrent().getPage().setTitle(DemoUtils.getViewName(viewClass) + " - " + "Crud UI add-on demo");
	}

	public void addSourceCodeAnchorToCurrentView() {
		Class<? extends HasComponents> viewClass = tabToView.get(tabs.getSelectedTab());
		if (!HomeView.class.equals(viewClass)) {
			HorizontalLayout footer = new HorizontalLayout(
					new Html("<span>Source code 👉&nbsp;</span>"),
					new Anchor(DemoUtils.getGitHubLink(viewClass), viewClass.getSimpleName() + ".java"));
			footer.setSpacing(false);
			((HasComponents) getContent()).add(footer);
		}
	}

}
