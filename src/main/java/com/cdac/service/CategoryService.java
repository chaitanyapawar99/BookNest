package com.cdac.service;

import java.util.List;

import com.cdac.dto.CategoryDTO;

public interface CategoryService 
{
	CategoryDTO createCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Long id);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
