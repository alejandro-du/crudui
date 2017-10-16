import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
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
import org.apache.bval.util.StringUtils;
import org.vaadin.crudui.crud.Crud;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.crud.impl.EditableGridCrud;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;
import org.vaadin.jetty.VaadinJettyServer;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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
        addCrud(getEditableGridCrud(), "Editable Grid");
    }

    private void addCrud(Crud crud, String caption) {
        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        tabSheet.addTab(layout, caption);
    }

    private Crud getDefaultCrud() {
        return new GridCrud<>(User.class, this);
    }

    private Crud getDefaultCrudWithFixes() {
        GridCrud<User> crud = new GridCrud<>(User.class);
        crud.setCrudListener(this);
        crud.getCrudFormFactory().setFieldProvider("groups", new CheckBoxGroupProvider<>(groups));
        crud.getCrudFormFactory().setFieldProvider("mainGroup", new ComboBoxProvider<>(groups));

        return crud;
    }

    private Crud getConfiguredCrud() {
        GridCrud<User> crud = new GridCrud<>(User.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        GridLayoutCrudFormFactory<User> formFactory = new GridLayoutCrudFormFactory<>(User.class, 2, 2);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));

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

        formFactory.setFieldProvider("groups", new CheckBoxGroupProvider<>("Groups", groups, Group::getName));
        formFactory.setFieldProvider("mainGroup", new ComboBoxProvider<>("Main Group", groups, Group::getName));

        formFactory.setButtonCaption(CrudOperation.ADD, "Add new user");
        crud.setRowCountCaption("%d user(s) found");

        return crud;
    }

    private Crud getEditableGridCrud() {
        EditableGridCrud<User> crud = new EditableGridCrud<>(User.class, this);

        crud.getGrid().setColumns("name", "birthDate", "email", "phoneNumber", "password", "groups", "mainGroup", "active");
        crud.getCrudFormFactory().setVisibleProperties("name", "birthDate", "email", "phoneNumber", "password", "groups", "mainGroup", "active");

        crud.getGrid().getColumn("password").setRenderer(user -> "********", new TextRenderer());
        crud.getGrid().getColumn("mainGroup").setRenderer(group -> ((Group) group).getName(), new TextRenderer());
        crud.getGrid().getColumn("groups").setRenderer(groups -> StringUtils.join(((Set<Group>) groups).stream().map(g -> g.getName()).collect(Collectors.toList()), ", "), new TextRenderer());

        crud.getCrudFormFactory().setFieldType("password", PasswordField.class);
        crud.getCrudFormFactory().setFieldProvider("groups", new CheckBoxGroupProvider<>(null, groups, group -> group.getName()));
        crud.getCrudFormFactory().setFieldProvider("mainGroup", new ComboBoxProvider<>(null, groups, group -> group.getName()));

        crud.getCrudFormFactory().setUseBeanValidation(true);

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
