package org.example.dachuang.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob // 对于较长文本
    private String description;

    @Column(nullable = false)
    private Double price;

    private String imageUrl;
    private Integer stock;
    private Boolean isRecommended = false;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER加载分类信息
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}