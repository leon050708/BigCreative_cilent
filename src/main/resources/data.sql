INSERT INTO category (name) VALUES ('电子产品');
INSERT INTO category (name) VALUES ('家居办公');
INSERT INTO category (name) VALUES ('图书');

INSERT INTO product (name, description, price, image_url, stock, is_recommended, category_id) VALUES
                                                                                                  ('高性能笔记本电脑', '最新款，速度快，性能强劲。', 7999.00, 'https://via.placeholder.com/300x200?text=Laptop', 10, true, 1),
                                                                                                  ('舒适办公椅', '人体工程学设计，久坐不累。', 899.00, 'https://via.placeholder.com/300x200?text=Office+Chair', 25, true, 2),
                                                                                                  ('Vue.js从入门到精通', '学习Vue.js的最佳实践。', 79.00, 'https://via.placeholder.com/300x200?text=Vue+Book', 50, false, 3),
                                                                                                  ('智能降噪耳机', '沉浸式音乐体验。', 1299.00, 'https://via.placeholder.com/300x200?text=Headphones', 15, true, 1);