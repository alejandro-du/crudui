package org.vaadin.crudui.app;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        System.out.println("Creating test data...");

        Stream.of("Services,IT,HR,Management,Marketing,Sales,Operations,Finance".split(","))
                .map(name -> new Group(name))
                .forEach(GroupRepository::save);


        List<Group> allGroups = GroupRepository.findAll();

        String[] firstNames = "Maria,Nicole,Sandra,Brenda,Clare,Cathy,Elizabeth,Tom,John,Daniel,Edward,Hank,Arthur,Bill".split(",");
        String[] lastNames = "Smith,Johnson,Williams,Jones,Brown,Miller,Wilson,Wright,Thompson,Lee".split(",");

        Random rand = new Random();

        IntStream.rangeClosed(1, 200)
                .mapToObj(i -> {
                    String name = firstNames[rand.nextInt(firstNames.length)] + " " + lastNames[rand.nextInt(lastNames.length)];
                    ArrayList<Group> groups = IntStream.rangeClosed(1, 1 + rand.nextInt(2))
                            .mapToObj(j -> allGroups.get(rand.nextInt(allGroups.size())))
                            .collect(Collectors.toCollection(ArrayList::new));

                    return new User(
                            name,
                            LocalDate.now().minusDays(365 * 10),
                            rand.nextInt(9000000) + 1000000,
                            name.replace(" ", "").toLowerCase() + i + "@test.com",
                            UUID.randomUUID().toString(),
                            rand.nextInt(10) > 0,
                            groups.get(rand.nextInt(groups.size())),
                            new HashSet<>(groups),
                            MaritalStatus.values()[rand.nextInt(MaritalStatus.values().length)]
                    );
                })
                .forEach(UserRepository::save);
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
