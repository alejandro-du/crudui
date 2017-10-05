import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.TextRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.crudui.crud.CrudComponent;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
import org.vaadin.crudui.form.impl.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;
import org.vaadin.jetty.VaadinJettyServer;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Alejandro Duarte
 */
@Theme(ValoTheme.THEME_NAME)
public class TestUI extends UI implements CrudListener<User> {

    private static AtomicLong nextId = new AtomicLong(1);

    public static void main(String[] args) throws Exception {
        VaadinJettyServer server = new VaadinJettyServer(8080, TestUI.class);
        server.start();

        for (int i = 1; i < 5; i++) {
            Group group = new Group("Group " + i, false);
            groups.add(group);
        }

        while (nextId.get() <= 50) {
            users.add(new User(
                    nextId.get(),
                    "User " + nextId,
                    Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    "email" + nextId + "@test.com",
                    nextId.intValue() * 101001,
                    "password" + nextId,
                    true,
                    groups.stream().findFirst().get(),
                    groups));

            nextId.incrementAndGet();
        }
    }

    private static Set<User> users = new LinkedHashSet<>();
    private static Set<Group> groups = new LinkedHashSet<>();

    private TabSheet tabSheet = new TabSheet();

    @Override
    protected void init(VaadinRequest request) {
        tabSheet.setSizeFull();
        setContent(tabSheet);

        addCrud(getDefaultCrud(), "Default");
        addCrud(getDefaultCrudWithFixes(), "Default (with fixes)");
        addCrud(getConfiguredCrud(), "Configured");
    }

    private void addCrud(CrudComponent crud, String caption) {
        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        tabSheet.addTab(layout, caption);
    }

    private CrudComponent getDefaultCrud() {
        return new GridBasedCrudComponent<>(User.class, this);
    }

    private CrudComponent getDefaultCrudWithFixes() {
        GridBasedCrudComponent<User> crud = new GridBasedCrudComponent<>(User.class);
        crud.setCrudListener(this);

        crud.getCrudFormFactory().setFieldProvider("groups", () -> {
            CheckBoxGroup<Group> checkBoxes = new CheckBoxGroup("Groups", groups);
            checkBoxes.setItemCaptionGenerator(Group::getName);
            return checkBoxes;
        });

        crud.getCrudFormFactory().setFieldProvider("mainGroup", () -> {
            ComboBox<Group> comboBox = new ComboBox<>("Main group", groups);
            comboBox.setItemCaptionGenerator(Group::getName);
            return comboBox;
        });

        return crud;
    }

    private CrudComponent getConfiguredCrud() {
        GridBasedCrudComponent<User> crud = new GridBasedCrudComponent<>(User.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        GridLayoutCrudFormFactory<User> formFactory = new GridLayoutCrudFormFactory<>(User.class, 2, 2);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        formFactory.setVisibleProperties(CrudOperation.READ, "id", "name", "birthDate", "email", "phoneNumber", "groups", "active", "mainGroup");
        formFactory.setVisibleProperties(CrudOperation.ADD, "name", "birthDate", "email", "phoneNumber", "groups", "password", "mainGroup", "active");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "id", "name", "birthDate", "email", "phoneNumber", "password", "groups", "active", "mainGroup");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "name", "email", "phoneNumber");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("name", "birthDate", "email", "phoneNumber", "mainGroup", "active");
        crud.getGrid().getColumn("mainGroup").setRenderer(group -> group == null ? "" : ((Group) group).getName(), new TextRenderer());
        ((Grid.Column<User, Date>) crud.getGrid().getColumn("birthDate")).setRenderer(new DateRenderer("%1$tY-%1$tm-%1$te"));

        formFactory.setFieldType("password", PasswordField.class);
        formFactory.setFieldCreationListener("birthDate", field -> ((DateField) field).setDateFormat("yyyy-MM-dd"));
        formFactory.setFieldProvider("groups", () -> {
            CheckBoxGroup<Group> checkboxes = new CheckBoxGroup<>("Groups", groups);
            checkboxes.setItemCaptionGenerator(Group::getName);
            return checkboxes;
        });
        formFactory.setFieldProvider("mainGroup", () -> {
            ComboBox<Group> comboBox = new ComboBox<>("Main group", groups);
            comboBox.setItemCaptionGenerator(Group::getName);
            return comboBox;
        });

        formFactory.setButtonCaption(CrudOperation.ADD, "Add new user");
        crud.setRowCountCaption("%d user(s) found");
        crud.setErrorConsumer(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));

        return crud;
    }

    @Override
    public User add(User user) {
        user.setId(nextId.getAndIncrement());
        users.add(user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId().equals(1l)) {
            throw new RuntimeException("A simulated error has occurred");
        }
        return user;
    }

    @Override
    public void delete(User user) {
        users.remove(user);
    }

    @Override
    public Collection<User> findAll() {
        return users;
    }

}
