package org.vaadin.crudui.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.crudui.demo.entity.Technology;
import org.vaadin.crudui.demo.repository.TechnologyRepository;

/**
 * @author Boniface Chacha
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TechnologyService {

	private final TechnologyRepository technologyRepository;

	public TechnologyService(TechnologyRepository technologyRepository) {
		this.technologyRepository = technologyRepository;
	}

	@Transactional
	public Technology save(Technology technology) {
		validate(technology);
		technology = technologyRepository.save(technology);
		return technology;
	}

	private void validate(Technology technology) {
		Technology parent = technology.getParent();

		// if is its on parent
		if (parent != null && parent.equals(technology))
			throw new IllegalArgumentException("Technology can not be its own parent");
	}

	@Transactional
	public void delete(Technology technology) {
		List<Technology> children = findChildren(technology);
		children.forEach(this::delete);
		technologyRepository.delete(technology);
	}

	public List<Technology> findChildren(Technology technology) {
		return technologyRepository.findAllByParent(technology);
	}

	public List<Technology> findRoots() {
		return technologyRepository.findAllByParentIsNull();
	}

	public List<Technology> findAll() {
		return technologyRepository.findAll();
	}

	public List<Technology> getLeaves() {
		List<Technology> categories = findAll();

		Set<Technology> parents = new HashSet<>();
		categories.forEach(technology -> {
			if (technology.hasParent())
				parents.add(technology.getParent());
		});

		return categories.stream().filter(technology -> {
			return !parents.contains(technology);
		}).collect(Collectors.toList());
	}

	public Technology getTechnology(Long technologyId) {
		return technologyRepository.findById(technologyId)
				.orElseThrow(() -> new IllegalArgumentException("There is no technology with id " + technologyId));
	}
}
