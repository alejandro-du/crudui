import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.crudui.crud.CrudComponent;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.impl.GridBasedCrudComponent;
import org.vaadin.crudui.form.impl.GridLayoutCrudFormFactory;
import org.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;
import org.vaadin.jetty.VaadinJettyServer;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Alejandro Duarte
 */
@Theme(ValoTheme.THEME_NAME)
public class TestUI extends UI implements CrudListener<User> {

    public static void main(String[] args) throws Exception {
        VaadinJettyServer server = new VaadinJettyServer(8080, TestUI.class);
        server.start();

        Group employees = new Group("Employees", false);
        Group admins = new Group("Admins", true);
        groups.add(employees);
        groups.add(admins);

        for (long i = 1; i <= 20; i++) {
            users.add(new User("User " + i, new Date(), "email" + i + "@test.com", "password" + i, true, employees, groups));
        }
    }

    private static Set<User> users = new LinkedHashSet<>();
    private static Set<Group> groups = new LinkedHashSet<>();

    private TabSheet tabSheet = new TabSheet();

    @Override
    protected void init(VaadinRequest request) {
        tabSheet.setSizeFull();
        setContent(tabSheet);

        addCrud(getDefaultCrudWithNoListeners(), "Default (no listeners)");
        addCrud(getDefaultCrudWithListeners(), "Default (with listeners)");
        addCrud(getDefaultCrudWithFixes(), "Default (with fixes)");
        addCrud(getConfiguredCrud(), "Configured");
    }

    private void addCrud(CrudComponent crud, String caption) {
        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        tabSheet.addTab(layout, caption);
    }

    private CrudComponent getDefaultCrudWithNoListeners() {
        return new GridBasedCrudComponent<>(User.class);
    }

    private CrudComponent getDefaultCrudWithListeners() {
        GridBasedCrudComponent<User> crud = new GridBasedCrudComponent<>(User.class);
        crud.setCrudListener(this);

        return crud;
    }

    private CrudComponent getDefaultCrudWithFixes() {
        GridBasedCrudComponent<User> crud = new GridBasedCrudComponent<>(User.class);
        crud.setCrudListener(this);

        crud.setFieldProvider("groups", () -> {
            OptionGroup optionGroup = new OptionGroup();
            optionGroup.setMultiSelect(true);
            optionGroup.setContainerDataSource(new BeanItemContainer<>(Group.class, groups));
            optionGroup.setItemCaptionPropertyId("name");
            return optionGroup;
        });

        crud.setFieldProvider("mainGroup", () -> {
            ComboBox comboBox = new ComboBox();
            comboBox.setContainerDataSource(new BeanItemContainer<>(Group.class, groups));
            comboBox.setItemCaptionPropertyId("name");
            return comboBox;
        });

        return crud;
    }

    private CrudComponent getConfiguredCrud() {
        GridBasedCrudComponent<User> crud = new GridBasedCrudComponent<>(User.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        crud.setCrudFormFactory(new GridLayoutCrudFormFactory<>(2, 2));

        crud.setAddFormVisiblePropertyIds("name", "birthDate", "email", "password", "groups", "mainGroup", "active");
        crud.setUpdateFormVisiblePropertyIds("name", "birthDate", "email", "groups", "mainGroup", "active");
        crud.setDeleteFormVisiblePropertyIds("name", "email", "active");

        crud.getGridContainer().addNestedContainerBean("mainGroup");
        crud.getGrid().setColumns("name", "birthDate", "email", "mainGroup.name", "active");
        crud.getGrid().getColumn("mainGroup.name").setHeaderCaption("Main group");
        crud.getGrid().getColumn("birthDate").setRenderer(new DateRenderer("%1$tY-%1$tm-%1$te"));

        crud.setFieldType("password", PasswordField.class);
        crud.setFieldCreationListener("birthDate", field -> ((DateField) field).setDateFormat("yyyy-MM-dd"));
        crud.setFieldProvider("groups", () -> {
            OptionGroup optionGroup = new OptionGroup();
            optionGroup.setMultiSelect(true);
            optionGroup.setContainerDataSource(new BeanItemContainer<>(Group.class, groups));
            optionGroup.setItemCaptionPropertyId("name");
            return optionGroup;
        });
        crud.setFieldProvider("mainGroup", () -> {
            ComboBox comboBox = new ComboBox();
            comboBox.setContainerDataSource(new BeanItemContainer<>(Group.class, groups));
            comboBox.setItemCaptionPropertyId("name");
            return comboBox;
        });

        crud.setAddCaption("Add new user");
        crud.setRowCountCaption("%d user(s) found");

        return crud;
    }

    @Override
    public void add(User user) {
        users.add(user);
    }

    @Override
    public void update(User user) {
        // nothing to do
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
