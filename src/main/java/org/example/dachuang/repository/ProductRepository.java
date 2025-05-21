package org.example.dachuang.repository;


import org.example.dachuang.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // <--- 导入 @Query
import org.springframework.data.repository.query.Param; // <--- 导入 @Param
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByIsRecommended(Boolean isRecommended);
    // 简单搜索，可扩展为更复杂的Specification
    @Query(value = "SELECT * FROM product p WHERE LOWER(p.name) LIKE :processedSearchTerm OR LOWER(p.description) LIKE :processedSearchTerm",
            nativeQuery = true)
    List<Product> findByFieldsContaining(@Param("processedSearchTerm") String processedSearchTerm);
}
