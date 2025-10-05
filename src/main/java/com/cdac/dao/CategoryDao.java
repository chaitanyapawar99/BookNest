package com.cdac.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.entities.Category;

public interface CategoryDao extends JpaRepository<Category, Long>
{
	// Check if category name already exists
    boolean existsByNameIgnoreCase(String name);

    // Search categories by partial name
    List<Category> findByNameContainingIgnoreCase(String keyword);
}
