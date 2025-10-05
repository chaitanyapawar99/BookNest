package com.cdac.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.dao.CategoryDao;
import com.cdac.dto.CategoryDTO;
import com.cdac.entities.Category;
import com.cdac.custom_exceptions.ResourceNotFoundException;
import com.cdac.custom_exceptions.InvalidInputException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;
    private final ModelMapper modelMapper;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // prevent duplicates
        if (categoryDao.existsByNameIgnoreCase(categoryDTO.getName())) {
            throw new InvalidInputException("Category already exists with name: " + categoryDTO.getName());
        }

        Category category = modelMapper.map(categoryDTO, Category.class);
        Category saved = categoryDao.save(category);
        log.info("Created category id={} name={}", saved.getId(), saved.getName());
        return modelMapper.map(saved, CategoryDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryDao.findAll().stream()
            .map(cat -> modelMapper.map(cat, CategoryDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Category cat = categoryDao.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return modelMapper.map(cat, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        Category updated = categoryDao.save(category);
        log.info("Updated category id={}", updated.getId());
        return modelMapper.map(updated, CategoryDTO.class);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (category.getBooks() != null && !category.getBooks().isEmpty()) {
            throw new InvalidInputException("Category contains books. Reassign or remove books before deleting.");
        }

        categoryDao.delete(category);
        log.info("Deleted category id={}", id);
    }
}
