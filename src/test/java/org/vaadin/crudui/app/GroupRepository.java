package org.vaadin.crudui.app;

import java.util.List;

/**
 * @author Alejandro Duarte
 */
public class GroupRepository {

    public static List<Group> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select g from Group g").getResultList()
        );
    }

    public static Group save(Group group) {
        return JPAService.runInTransaction(em -> em.merge(group));
    }

}
