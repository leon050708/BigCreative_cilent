package org.example.dachuang.controller;

import org.example.dachuang.dto.ProductAdminRequestDto;
import org.example.dachuang.dto.ProductDto;
import org.example.dachuang.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.security.access.prepost.PreAuthorize; // 示例：用于方法级别安全

import java.util.List;

@RestController
@RequestMapping("/api/admin/products") // 管理端接口统一前缀
// @PreAuthorize("hasRole('ADMIN')") // 示例：整个Controller都需要ADMIN权限 (需要在Spring Security中配置)
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductAdminRequestDto productRequestDto) {
        ProductDto createdProduct = productService.createProduct(productRequestDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductDto>> getAllProductsForAdmin() {
        return ResponseEntity.ok(productService.adminGetAllProducts());
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> getProductByIdForAdmin(@PathVariable Long id) {
        // 可以复用公共的 getProductById，如果返回内容一致
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductAdminRequestDto productRequestDto) {
        ProductDto updatedProduct = productService.adminUpdateProduct(id, productRequestDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
