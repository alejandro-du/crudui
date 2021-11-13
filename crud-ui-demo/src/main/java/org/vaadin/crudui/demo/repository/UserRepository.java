package org.vaadin.crudui.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.crudui.demo.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameContainingIgnoreCase(String name);

}
