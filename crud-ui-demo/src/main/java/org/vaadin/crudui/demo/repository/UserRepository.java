package org.vaadin.crudui.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.crudui.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

	long countByNameContainingIgnoreCase(String name);

}
