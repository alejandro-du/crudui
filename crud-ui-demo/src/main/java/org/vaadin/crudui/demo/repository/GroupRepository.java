package org.vaadin.crudui.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.crudui.demo.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
