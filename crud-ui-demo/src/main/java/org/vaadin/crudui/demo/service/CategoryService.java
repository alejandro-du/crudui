package org.vaadin.crudui.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.crudui.demo.entity.Category;
import org.vaadin.crudui.demo.repository.CategoryRepository;

/**
 * @author Boniface Chacha
 * @email bonifacechacha@gmail.com
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CategoryService {

  private CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }


  @Transactional
  public Category save(Category category) {
    validate(category);
    category = categoryRepository.save(category);
    return category;
  }

  private void validate(Category category) {
    Category parent = category.getParent();

    //if is its on parent
    if (parent != null && parent.equals(category))
      throw new IllegalArgumentException("Category can not be its own parent");
  }

  @Transactional
  public void delete(Category category) {
    List<Category> children = findChildren(category);
    if (!children.isEmpty()) {
      for (Category child : children) {
        delete(child);
      }
    }
    categoryRepository.delete(category);

  }

  public List<Category> findChildren(Category category) {
    return categoryRepository.findAllByParent(category);
  }


  public List<Category> findRoots() {
    return categoryRepository.findAllByParentIsNull();
  }

  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  public List<Category> getLeaves() {
    List<Category> categories = findAll();

    Set<Category> parents = new HashSet<>();
    categories.forEach(category -> {
      if (category.hasParent())
        parents.add(category.getParent());
    });

    return categories.stream().filter(category -> {
      return !parents.contains(category);
    }).collect(Collectors.toList());
  }

  public Category getCategory(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("There is no category with id " + categoryId));
  }
}
