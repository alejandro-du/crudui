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
import org.vaadin.crudui.demo.ui.view.CrudWithSplitLayoutView;
import org.vaadin.crudui.demo.ui.view.CustomCrudView;
import org.vaadin.crudui.demo.ui.view.CustomTreeCrudView;
import org.vaadin.crudui.demo.ui.view.DefaultCrudView;
import org.vaadin.crudui.demo.ui.view.HomeView;

public class MainLayout extends AppLayout implements BeforeEnterObserver, AfterNavigationObserver {

	private VerticalLayout viewContainer = new VerticalLayout();
	private HorizontalLayout footer = new HorizontalLayout();
	private Tabs tabs = new Tabs();
	private Image logo = new Image("https://i.imgur.com/GPpnszs.png", "Vaadin Logo");
	private Button themeSwitch = new Button(VaadinIcon.MOON_O.create());
	private Map<Tab, Class<? extends HasComponents>> tabToView = new HashMap<>();
	private Map<Class<? extends HasComponents>, Tab> viewToTab = new HashMap<>();

	public MainLayout() {
		logo.setHeight("44px");

		tabs.addSelectedChangeListener(this::tabsSelectionChanged);
		addTab(HomeView.class);
		addTab(DefaultCrudView.class);
		addTab(CustomCrudView.class);
		addTab(CrudWithSplitLayoutView.class);
		addTab(CustomTreeCrudView.class);

		themeSwitch.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		themeSwitch.addClickListener(e -> switchTheme());

		var headerLayout = new HorizontalLayout(logo, tabs, themeSwitch);
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
		switchTheme();
	}

	@Override
	public void showRouterLayoutContent(HasElement content) {
		viewContainer.removeAll();
		viewContainer.add(content.getElement().getComponent().get());
        afterNavigation();
	}

	private void switchTheme() {
		if (UI.getCurrent().getElement().getAttribute("theme") == null) {
			UI.getCurrent().getElement().setAttribute("theme", "dark");
			themeSwitch.setIcon(VaadinIcon.SUN_O.create());
			logo.setSrc("https://i.imgur.com/3xp1nLI.png");
		} else {
			UI.getCurrent().getElement().removeAttribute("theme");
			themeSwitch.setIcon(VaadinIcon.MOON_O.create());
			logo.setSrc("https://i.imgur.com/GPpnszs.png");
		}
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
			footer.removeAll();
			footer.add(
					new Html("<span>Source code 👉&nbsp;</span>"),
					new Anchor(DemoUtils.getGitHubLink(viewClass), viewClass.getSimpleName() + ".java")
			);
		}
	}

}
