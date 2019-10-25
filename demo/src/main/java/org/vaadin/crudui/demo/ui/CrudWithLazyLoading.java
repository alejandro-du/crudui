package org.vaadin.crudui.demo.ui;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.crud.LazyCrudListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.demo.entity.Group;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.repository.GroupRepository;
import org.vaadin.crudui.demo.repository.UserRepository;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.data.spring.OffsetBasedPageRequest;

@Route("lazy-loading")
public class CrudWithLazyLoading extends VerticalLayout {

    public CrudWithLazyLoading(UserRepository userRepository, GroupRepository groupRepository) {
        // crud instance
        GridCrud<User> crud = new GridCrud<>(User.class);

        // grid configuration
        crud.getGrid().setColumns("name", "birthDate", "maritalStatus", "email", "phoneNumber", "active");
        crud.getGrid().setPageSize(50);

        // form configuration
        crud.getCrudFormFactory().setVisibleProperties(
                "name", "birthDate", "email", "salary", "phoneNumber", "maritalStatus", "groups", "active", "mainGroup");
        crud.getCrudFormFactory().setFieldProvider("mainGroup",
                new ComboBoxProvider<>(groupRepository.findAll()));
        crud.getCrudFormFactory().setFieldProvider("groups",
                new CheckBoxGroupProvider<>(groupRepository.findAll()));
        crud.getCrudFormFactory().setFieldProvider("groups",
                new CheckBoxGroupProvider<>("Groups", groupRepository.findAll(), Group::getName));
        crud.getCrudFormFactory().setFieldProvider("mainGroup",
                new ComboBoxProvider<>("Main Group", groupRepository.findAll(), new TextRenderer<>(Group::getName), Group::getName));

        // layout configuration
        setSizeFull();
        add(new H1("CRUD with lazy loading"), crud, new Anchor(Util.getGitHubLink(this.getClass()), "Source code"));

        // logic configuration
        crud.setCrudListener(new LazyCrudListener<User>() {
            @Override
            public DataProvider<User, Void> getDataProvider() {
                return DataProvider.fromCallbacks(
                        query -> userRepository.findAll(new OffsetBasedPageRequest(query)).stream(),
                        query -> (int) userRepository.count()
                );
            }

            @Override
            public User add(User user) {
                return userRepository.save(user);
            }

            @Override
            public User update(User user) {
                return userRepository.save(user);
            }

            @Override
            public void delete(User user) {
                userRepository.delete(user);
            }
        });
    }

}
