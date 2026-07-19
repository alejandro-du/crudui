package org.vaadin.crudui.demo.ui.view.test;

import java.math.BigDecimal;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;
import org.vaadin.crudui2.Crud;

/**
 * Test view demonstrating requirement-0012:
 * Create mode can use a custom supplier instead of requiring a public no-args constructor.
 */
@Route(value = "requirement-0012")
public class Requirement0012 extends VerticalLayout {

    public Requirement0012(UserService userService, GroupService groupService) {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        Crud<User> crud = Crud.of(User.class)
                .form().newInstanceSupplier(this::newUserWithDefaults)
                .onRead(userService::findAll)
                .onCreate(userService::save)
                .onUpdate(userService::save)
                .onDelete(userService::delete)
                .build();

        add(
                new H3("Requirement 0012 - Custom new instance supplier"),
                new Paragraph("This view follows the requirement example style: form().newInstanceSupplier(...) "
                        + "provides default values for create mode while CRUD operations use the real services."),
                crud);
        expand(crud);
    }

    private User newUserWithDefaults() {
        User user = new User();
        user.setEmail("@example.com");
        user.setSalary(BigDecimal.valueOf(3000));
        user.setPassword("password123");
        user.setActive(true);

        return user;
    }
}
