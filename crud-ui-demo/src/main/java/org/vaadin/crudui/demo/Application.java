package org.vaadin.crudui.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.vaadin.crudui.demo.entity.Category;
import org.vaadin.crudui.demo.entity.Group;
import org.vaadin.crudui.demo.entity.MaritalStatus;
import org.vaadin.crudui.demo.entity.User;
import org.vaadin.crudui.demo.service.CategoryService;
import org.vaadin.crudui.demo.service.GroupService;
import org.vaadin.crudui.demo.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application {

    public static final int DEMO_USERS_COUNT = UserService.USERS_COUNT_LIMIT / 2;

    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> initDatabase(GroupService groupService, UserService userService,
            CategoryService categoryService) {
        return event -> {
            if (groupService.count() == 0) {
                createDemoData(groupService, userService, categoryService);
            }
        };
    }

    private void createDemoData(GroupService groupService, UserService userService, CategoryService categoryService) {
        log.info("Creating demo data...");

        Stream.of("Services,IT,HR,Management,Marketing,Sales,Operations,Finance".split(","))
                .map(name -> new Group(name))
                .forEach(groupService::save);

        List<Group> allGroups = groupService.findAll();

        groupService.findAll();

        String[] firstNames = "Maria,Nicole,Sandra,Brenda,Clare,Cathy,Elizabeth,Tom,John,Daniel,Edward,Hank,Arthur,Bill"
                .split(",");
        String[] lastNames = "Smith,Johnson,Williams,Jones,Brown,Miller,Wilson,Wright,Thompson,Lee".split(",");

        Random rand = new Random();

        IntStream.rangeClosed(1, DEMO_USERS_COUNT)
                .mapToObj(i -> {
                    String name = firstNames[rand.nextInt(firstNames.length)] + " "
                            + lastNames[rand.nextInt(lastNames.length)];
                    ArrayList<Group> groups = IntStream.rangeClosed(1, 1 + rand.nextInt(2))
                            .mapToObj(j -> allGroups.get(rand.nextInt(allGroups.size())))
                            .collect(Collectors.toCollection(ArrayList::new));

                    return new User(
                            name,
                            LocalDate.now().minusDays(365 * 10),
                            rand.nextInt(9000000) + 1000000,
                            name.replace(" ", "").toLowerCase() + i + "@test.com",
                            BigDecimal.valueOf(5000),
                            UUID.randomUUID().toString(),
                            rand.nextInt(10) > 0,
                            groups.get(rand.nextInt(groups.size())),
                            new HashSet<>(groups),
                            MaritalStatus.values()[rand.nextInt(MaritalStatus.values().length)]);
                })
                .forEach(userService::save);

        String[] languages = new String[] { "Java", "Javascript", "Dart" };
        String[][] frameworks = new String[][] {
                { "Vaadin", "Spring", "Guice" },
                { "Hilla", "React", "Svelte" },
                { "Flutter" }
        };

        for (int i = 0; i < languages.length; i++) {
            Category language = categoryService.save(new Category(languages[i], languages[i], null));
            for (int j = 0; j < frameworks[i].length; j++) {
                categoryService.save(new Category(frameworks[i][j], frameworks[i][j], language));
            }
        }

        log.info("Demo data created.");
    }

}
