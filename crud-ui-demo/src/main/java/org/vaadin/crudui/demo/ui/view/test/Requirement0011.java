package org.vaadin.crudui.demo.ui.view.test;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui2.Crud;
import org.vaadin.crudui2.list.impl.GridList;

/**
 * Test view demonstrating requirement-0011:
 * - addFilterComponent(Component)
 * - onSaveSuccess(...)
 * - onDeleteSuccess(...)
 * - onOperationError(...)
 */
@Route(value = "requirement-0011")
public class Requirement0011 extends VerticalLayout {

    public Requirement0011(UserService userService, GroupService groupService) {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        Grid<User> userGrid = new Grid<>(User.class, true);

        TextField nameFilter = new TextField();
        nameFilter.setPlaceholder("Search by name...");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setWidthFull();
        nameFilter.setValueChangeMode(ValueChangeMode.EAGER);

        DataProvider<User, Void> filteredDataProvider = DataProvider.fromCallbacks(
                query -> userService.findByNameContainingIgnoreCase(
                        normalizeFilter(nameFilter.getValue()), query.getPage(), query.getPageSize()).stream(),
                query -> (int) userService.countByNameContainingIgnoreCase(normalizeFilter(nameFilter.getValue())));

        Crud<User> userCrud = Crud.of(User.class)
                .list(GridList.of(User.class).grid(userGrid))
                .onRead(filteredDataProvider)
                .onCreate(userService::save)
                .onUpdate(userService::save)
                .onDelete(userService::delete)
                .addFilterComponent(nameFilter)
                .onSaveSuccess(user -> Notification.show("Saved: " + user.getName()))
                .onDeleteSuccess(user -> Notification.show("Deleted: " + user.getName()))
                .onOperationError(error -> Notification.show(error.getMessage(), 3000, Position.MIDDLE))
                .build();

            nameFilter.addValueChangeListener(event -> userGrid.getDataProvider().refreshAll());

        add(userCrud);
        expand(userCrud);
    }

    private static String normalizeFilter(String filterText) {
        return filterText == null ? "" : filterText.trim();
    }
}
