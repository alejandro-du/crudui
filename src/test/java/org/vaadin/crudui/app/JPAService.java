package org.vaadin.crudui.app;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Alejandro Duarte
 */
public class JPAService {

    private static EntityManagerFactory factory;

    public static void init() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory("test-pu");
            createTestData();
        }
    }

    private static void createTestData() {
        for (int i = 1; i < 5; i++) {
            Group group = new Group();
            group.setName("Group " + i);
            group.setAdmin(false);
            GroupRepository.save(group);
        }

        Set<Group> groups = GroupRepository.findAll().stream()
                .filter(r -> r.getId() <= 2)
                .collect(Collectors.toSet());

        for (int i = 1; i <= 50; i++) {
            User user = new User();
            user.setName("User " + i);
            user.setBirthDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            user.setEmail("email" + i + "@test.com");
            user.setPhoneNumber(i * 101001);
            user.setPassword("password" + i);
            user.setActive(true);
            user.setGroups(groups);
            user.setMainGroup(groups.stream().findFirst().get());

            UserRepository.save(user);
        }
    }

    public static void close() {
        factory.close();
    }

    public static EntityManagerFactory getFactory() {
        return factory;
    }

    public static <T> T runInTransaction(Function<EntityManager, T> function) {
        EntityManager entityManager = null;

        try {
            entityManager = JPAService.getFactory().createEntityManager();
            entityManager.getTransaction().begin();

            T result = function.apply(entityManager);

            entityManager.getTransaction().commit();
            return result;

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

}
