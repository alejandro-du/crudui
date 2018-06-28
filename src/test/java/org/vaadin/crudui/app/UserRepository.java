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

    public static List<User> findByNameLike(String name, Group group, int offset, int limit) {
        return JPAService.runInTransaction(em -> {
                    Query query = em.createQuery("select u from User u where lower(name) like lower(:name) and (:group is null or :group = u.mainGroup)");
                    query.setParameter("name", "%" + name + "%");
                    query.setParameter("group", group);
                    query.setFirstResult(offset);
                    query.setMaxResults(limit);
                    return query.getResultList();
                }
        );
    }

    public static int countByNameLike(String name, Group group) {
        return JPAService.runInTransaction(em -> {
                    Query query = em.createQuery("select count(u.id) from User u where lower(name) like lower(:name) and (:group is null or :group = u.mainGroup)");
                    query.setParameter("name", "%" + name + "%");
                    query.setParameter("group", group);
                    int i = ((Long) query.getSingleResult()).intValue();
                    return i;
                }
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
