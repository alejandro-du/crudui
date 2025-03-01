package org.vaadin.crudui.demo.ui;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import org.vaadin.crudui.demo.DemoUtils;
import org.vaadin.crudui.demo.ui.view.CustomizedView;
import org.vaadin.crudui.demo.ui.view.DefaultView;
import org.vaadin.crudui.demo.ui.view.HomeView;
import org.vaadin.crudui.demo.ui.view.TreeView;

@JsModule("theme-handler.js")
public class MainLayout extends AppLayout implements BeforeEnterObserver, AfterNavigationObserver {

	private VerticalLayout viewContainer = new VerticalLayout();
	private HorizontalLayout footer = new HorizontalLayout();
	private Tabs tabs = new Tabs();
	private Image logo = new Image();
	private Button themeSwitcher = new Button(VaadinIcon.MOON_O.create());
	private Map<Tab, Class<? extends HasComponents>> tabToView = new HashMap<>();
	private Map<Class<? extends HasComponents>, Tab> viewToTab = new HashMap<>();

	public MainLayout() {
		logo.addClassName("logo");
		logo.setHeight("44px");

		tabs.addSelectedChangeListener(this::tabsSelectionChanged);
		addTab(HomeView.class);
		addTab(CustomizedView.class);
		addTab(DefaultView.class);
		addTab(TreeView.class);

		themeSwitcher.setId("theme-switch");
		themeSwitcher.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		themeSwitcher.addClickListener(e -> UI.getCurrent().getPage().executeJs("window.switchTheme()"));

		var headerLayout = new HorizontalLayout(logo, tabs, themeSwitcher);
		headerLayout.setMargin(true);
		headerLayout.setWidthFull();
		headerLayout.expand(tabs);
		addToNavbar(headerLayout);

		viewContainer.setSizeFull();
		viewContainer.setPadding(false);

		footer.setSpacing(false);
		footer.setMargin(false);
		footer.setPadding(true);

		var content = new VerticalLayout();
		content.setSizeFull();
		content.setPadding(false);
		content.setMargin(false);
		content.setSpacing(false);
		content.add(viewContainer, footer);

		setContent(content);
		UI.getCurrent().getPage().executeJs("window.applySystemTheme()");
	}

	@Override
	public void showRouterLayoutContent(HasElement content) {
		viewContainer.removeAll();
		viewContainer.add(content.getElement().getComponent().get());
		afterNavigation();
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
		footer.removeAll();
		Class<? extends HasComponents> viewClass = tabToView.get(tabs.getSelectedTab());
		if (!HomeView.class.equals(viewClass)) {
			footer.add(
					new Html("<span>Source code 👉&nbsp;</span>"),
					new Anchor(DemoUtils.getGitHubLink(viewClass), viewClass.getSimpleName() + ".java"));
		}
	}

}
