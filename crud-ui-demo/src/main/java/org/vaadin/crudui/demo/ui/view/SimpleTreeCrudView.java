package org.vaadin.crudui.demo.ui.view;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.crud.impl.TreeGridCrud;
import org.vaadin.crudui.demo.entity.Category;
import org.vaadin.crudui.demo.service.CategoryService;
import org.vaadin.crudui.demo.ui.MainLayout;

/**
 * @author XakepSDK
 */
@Route(value = "simple-tree", layout = MainLayout.class)
public class SimpleTreeCrudView extends VerticalLayout {

    protected final CategoryService categoryService;

    public SimpleTreeCrudView(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostConstruct
    private void build() {
        TreeGridCrud<Category> crud = new TreeGridCrud<Category>(Category.class);
        crud.getGrid().removeAllColumns();
        crud.getGrid().addHierarchyColumn(Category::getName)
                .setHeader("Name");

        crud.setChildItemProvider(category -> categoryService.findChildren(category));

        crud.getCrudFormFactory().setVisibleProperties("name", "parent", "description");

        crud.getCrudFormFactory().setFieldProvider("name", category -> createNameField());
        crud.getCrudFormFactory().setFieldProvider("parent", category -> createParentSelector());
        crud.getCrudFormFactory().setFieldProvider("description", category -> createDescriptionField());

        crud.setFindAllOperation(() -> categoryService.findRoots());

        crud.setAddOperation(category -> categoryService.save(category));
        crud.setUpdateOperation(category -> categoryService.save(category));
        crud.setDeleteOperation(category -> categoryService.delete(category));

        crud.setChildItemProvider(category -> categoryService.findChildren(category));

        crud.setShowNotifications(false);
        addAndExpand(crud);
        setSizeFull();
    }

    private HasValueAndElement createNameField() {
        TextField nameField = new TextField("Name");
        nameField.setWidth("300px");
        return nameField;
    }

    private HasValueAndElement createDescriptionField() {
        TextArea descriptionField = new TextArea("Description");
        descriptionField.setWidth("300px");
        return descriptionField;
    }

    private HasValueAndElement createParentSelector() {
        ComboBox<Category> selector = new ComboBox<Category>("Parent", categoryService.findAll());
        selector.setWidth("300px");
        return selector;
    }

}
