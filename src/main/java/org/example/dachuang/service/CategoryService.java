package org.example.dachuang.service;

import org.example.dachuang.dto.CategoryDto;
import org.example.dachuang.exception.ResourceNotFoundException;
import org.example.dachuang.model.Category;
import org.example.dachuang.repository.CategoryRepository;
import org.example.dachuang.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private CategoryDto convertToCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        BeanUtils.copyProperties(category, categoryDto);
        // BeanUtils 应该会自动复制 mainCategory，因为字段名相同
        // 如果没有，可以手动设置: categoryDto.setMainCategory(category.getMainCategory());
        return categoryDto;
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setImageUrl(categoryDto.getImageUrl());
        category.setMainCategory(categoryDto.getMainCategory()); // 设置 mainCategory
        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryDto(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::convertToCategoryDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToCategoryDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }


    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setName(categoryDto.getName());
        category.setImageUrl(categoryDto.getImageUrl());
        category.setMainCategory(categoryDto.getMainCategory()); // 更新 mainCategory
        Category updatedCategory = categoryRepository.save(category);
        return convertToCategoryDto(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!productRepository.findByCategoryId(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete category with id: " + id + " as it has associated products.");
        }
        categoryRepository.delete(category);
    }
}