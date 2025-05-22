package org.example.dachuang.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;


    @Column(name = "image_url") // 数据库列名可以是 image_url
    private String imageUrl;
    // 如果需要双向关联，可以添加 @OneToMany List<Product> products;
    // 但为了简单起见，我们这里先不加，可以在Product中保留对Category的引用
}
