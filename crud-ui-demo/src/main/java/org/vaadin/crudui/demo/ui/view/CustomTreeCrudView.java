package org.vaadin.crudui.demo.ui.view;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.crud.impl.TreeGridCrud;
import org.vaadin.crudui.demo.entity.Technology;
import org.vaadin.crudui.demo.service.TechnologyService;
import org.vaadin.crudui.demo.ui.MainLayout;

@Route(value = "simple-tree", layout = MainLayout.class)
public class CustomTreeCrudView extends VerticalLayout {

	public CustomTreeCrudView(TechnologyService technologyService) {
		TreeGridCrud<Technology> crud = new TreeGridCrud<>(Technology.class);
		crud.getGrid().removeAllColumns();
		crud.getGrid().addHierarchyColumn(Technology::getName).setHeader("Name");
		crud.getGrid().addColumn(Technology::getVersion).setHeader("Version");
		crud.getGrid().addColumn(Technology::getLastPatchedAt).setHeader("Last Patched At");

		crud.setChildItemProvider(technologyService::findChildren);

		crud.getCrudFormFactory().setVisibleProperties("name", "version", "parent", "lastPatchedAt", "description");
		crud.getCrudFormFactory().setFieldProvider("description", technology -> new TextArea());
		crud.getCrudFormFactory().setFieldProvider("parent",
				technology -> new ComboBox<Technology>("Parent", technologyService.findAll()));

		crud.setFindAllOperation(technologyService::findRoots);

		crud.setAddOperation(technologyService::save);
		crud.setUpdateOperation(technologyService::save);
		crud.setDeleteOperation(technologyService::delete);

		crud.setChildItemProvider(technologyService::findChildren);

		crud.setShowNotifications(false);
		addAndExpand(crud);
		setSizeFull();
	}

}
