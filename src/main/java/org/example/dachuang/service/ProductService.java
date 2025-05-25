package org.example.dachuang.service;

import org.example.dachuang.dto.ProductAdminRequestDto;
import org.example.dachuang.dto.CategoryDto;
import org.example.dachuang.dto.ProductDto;
import org.example.dachuang.exception.ResourceNotFoundException;
import org.example.dachuang.model.Category;
import org.example.dachuang.model.Product;
import org.example.dachuang.repository.CategoryRepository;
import org.example.dachuang.repository.ProductRepository;
import org.springframework.beans.BeanUtils; // 用于对象属性复制
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // --- DTO转换辅助方法 ---
    private ProductDto convertToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        if (product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(product.getCategory(), categoryDto);
            productDto.setCategory(categoryDto);
        }
        return productDto;
    }

    private CategoryDto convertToCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        BeanUtils.copyProperties(category, categoryDto); // 这会复制包括 imageUrl 在内的所有匹配字段
        // 如果 imageUrl 字段名在 Category 和 CategoryDto 中不完全一样，需要手动设置：
        // categoryDto.setImageUrl(category.getImageUrl()); // 如果BeanUtils.copyProperties不能正确复制
        return categoryDto;
    }
    // --- End DTO转换 ---


    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts(Long categoryId, Boolean isRecommended, String searchTerm) {
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else if (isRecommended != null) {
            products = productRepository.findByIsRecommended(isRecommended);
        } else if (searchTerm != null && !searchTerm.isEmpty()) {
            String processedSearchTerm = "%" + searchTerm.toLowerCase() + "%";
            // 2. 调用新的 repository 方法
            products = productRepository.findByFieldsContaining(processedSearchTerm);
        } else {
            products = productRepository.findAll();
        }
        return products.stream().map(this::convertToProductDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToProductDto)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id)); // 稍后用自定义异常替换
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::convertToCategoryDto).collect(Collectors.toList());
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDetailsDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // 更新产品基本属性
        product.setName(productDetailsDto.getName());
        product.setDescription(productDetailsDto.getDescription());
        product.setPrice(productDetailsDto.getPrice());
        product.setImageUrl(productDetailsDto.getImageUrl());
        product.setStock(productDetailsDto.getStock());
        product.setIsRecommended(productDetailsDto.getIsRecommended() != null ? productDetailsDto.getIsRecommended() : false);

        // 更新分类
        if (productDetailsDto.getCategory() != null && productDetailsDto.getCategory().getId() != null) {
            Category category = categoryRepository.findById(productDetailsDto.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDetailsDto.getCategory().getId()));
            product.setCategory(category);
        } else if (productDetailsDto.getCategory() != null && productDetailsDto.getCategory().getName() != null && productDetailsDto.getCategory().getId() == null) {
            // 如果只提供了分类名但没有ID，可以尝试查找或创建，但这会使逻辑复杂化
            // 为简单起见，这里要求更新分类时提供分类ID
            throw new IllegalArgumentException("Category ID must be provided when updating product category.");
        }
        // 如果 productDetailsDto.getCategory() 为 null，则不更新分类 (根据业务需求定)


        Product updatedProduct = productRepository.save(product);
        return convertToProductDto(updatedProduct);
    }

    @Transactional
    public ProductDto createProduct(ProductAdminRequestDto requestDto) {
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDto.getCategoryId()));

        Product product = new Product();
        product.setName(requestDto.getName());
        product.setDescription(requestDto.getDescription());
        product.setPrice(requestDto.getPrice());
        product.setImageUrl(requestDto.getImageUrl());
        product.setStock(requestDto.getStock());
        product.setIsRecommended(requestDto.getIsRecommended() != null ? requestDto.getIsRecommended() : false);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return convertToProductDto(savedProduct);
    }

    @Transactional
    public ProductDto adminUpdateProduct(Long id, ProductAdminRequestDto requestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDto.getCategoryId()));

        product.setName(requestDto.getName());
        product.setDescription(requestDto.getDescription());
        product.setPrice(requestDto.getPrice());
        product.setImageUrl(requestDto.getImageUrl());
        product.setStock(requestDto.getStock());
        product.setIsRecommended(requestDto.getIsRecommended() != null ? requestDto.getIsRecommended() : false);
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return convertToProductDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        // 注意：如果订单项中有外键关联到产品，直接删除可能会失败或导致问题。
        // 可能需要先检查是否有订单关联，或者设置软删除。
        productRepository.delete(product);
    }

    // 管理端获取所有产品 (可能与公共接口返回内容或过滤条件不同)
    @Transactional(readOnly = true)
    public List<ProductDto> adminGetAllProducts() {
        return productRepository.findAll().stream().map(this::convertToProductDto).collect(Collectors.toList());
    }
}
