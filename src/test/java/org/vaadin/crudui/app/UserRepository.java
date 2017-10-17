package org.vaadin.crudui.app;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Alejandro Duarte
 */
public class UserRepository {

    public static List<User> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select u from User u").getResultList()
        );
    }

    public static User save(User user) {
        return JPAService.runInTransaction(em -> em.merge(user));
    }

    public static void delete(User user) {
        JPAService.runInTransaction(em -> {
            em.remove(getById(user.getId(), em));
            return null;
        });
    }

    private static User getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from User u where u.id=:id");
        query.setParameter("id", id);

        return (User) query.getResultList().stream().findFirst().orElse(null);
    }

}
