package org.vaadin.crudui.demo.ui.view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.flowingcode.vaadin.addons.markdown.MarkdownViewer;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.demo.ui.MainLayout;

@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

	public HomeView() throws IOException {
		Path readmePath = Paths.get(System.getProperty("user.dir"), "../README.md");
		String readmeContent = Files.readString(readmePath);

		add(
				new H1("Crud UI Add-on demo"),
				new Html("""
					<div>
						<p>
							This is the demo app for the
							<a href='https://vaadin.com/directory/component/crud-ui-add-on'>Crud UI add-on for Vaadin</a>.
							The full source code is
							<a href='https://github.com/alejandro-du/crudui/tree/master/demo'>available on GitHub</a>.
						</p>
						<p>
							Use the tabs above to navigate through the different demo views.
							There's a link to the implementation at the <b>bottom of each view</b>.
						</p>
					</div>
					"""),
				new MarkdownViewer(readmeContent)
		);
	}

}
