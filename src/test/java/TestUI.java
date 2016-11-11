import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.crudui.CrudComponent;
import org.vaadin.crudui.CrudListener;
import org.vaadin.crudui.impl.crud.GridBasedCrudComponent;
import org.vaadin.jetty.VaadinJettyServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Alejandro Duarte
 */
@Theme(ValoTheme.THEME_NAME)
public class TestUI extends UI implements CrudListener<User> {

    public static void main(String[] args) throws Exception {
        VaadinJettyServer server = new VaadinJettyServer(8080, TestUI.class);
        server.start();

        for (long i = 1; i <= 10; i++) {
            users.add(new User("User " + i, new Date(), "email" + i + "@test.com", "password" + i));
        }
    }

    private static ArrayList<User> users = new ArrayList<>();

    TabSheet tabSheet = new TabSheet();

    @Override
    protected void init(VaadinRequest request) {
        tabSheet.setSizeFull();
        setContent(tabSheet);

        addCrud(getDefaultCrudWithNoListeners(), "Default (no listeners)");
        addCrud(getDefaultCrudWithListeners(), "Default (with listeners)");
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

    private CrudComponent getConfiguredCrud() {
        GridBasedCrudComponent<User> crud = new GridBasedCrudComponent<>(User.class);
        crud.setCrudListener(this);
        crud.setVisiblePropertyIds("name", "birthDate", "email");
        crud.setAddFormVisiblePropertyIds("name", "birthDate", "email", "password");
        crud.setUpdateFormVisiblePropertyIds("name", "birthDate", "email", "password");
        crud.setAddCaption("Add new user");
        crud.setRowCountCaption("%d user(s) found");
        crud.getGrid().getColumn("birthDate").setRenderer(new DateRenderer("%1$te/%1$tm/%1$tY"));
        crud.setFieldType("password", PasswordField.class);
        crud.setFieldCreationListener("birthDate", field -> ((DateField) field).setDateFormat("yyyy-MM-dd"));

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
