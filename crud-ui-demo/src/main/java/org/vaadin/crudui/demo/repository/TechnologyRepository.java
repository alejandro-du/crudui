package org.vaadin.crudui.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.crudui.demo.entity.Technology;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {

	List<Technology> findAllByParent(Technology parent);

	List<Technology> findAllByParentIsNull();

}
