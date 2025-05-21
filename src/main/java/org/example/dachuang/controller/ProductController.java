package org.example.dachuang.controller;

import org.example.dachuang.dto.CategoryDto;
import org.example.dachuang.dto.ProductDto;
import org.example.dachuang.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/products")
// @CrossOrigin(origins = "http://localhost:5173") // 也可以在这里配置CORS，或全局配置
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean recommended,
            @RequestParam(required = false) String searchTerm) {
        return ResponseEntity.ok(productService.getAllProducts(categoryId, recommended, searchTerm));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDetailsDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productDetailsDto);
        return ResponseEntity.ok(updatedProduct);
    }

//    // 通常分类会单独一个Controller，这里为简单起见放在一起
//    @GetMapping("/categories") // 改为 /api/categories 会更好
//    public ResponseEntity<List<CategoryDto>> getAllCategories() {
//        // 将此端点移至 CategoryController 并使用 /api/categories 路径
//        return ResponseEntity.ok(productService.getAllCategories());
//    }
}