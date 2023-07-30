package com.cafe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(
        name = "Product.getAllProduct",
        query = "select new com.cafe.wrapper.ProductWrapper(p.id, p.name, p.description, p.prize, p.status, p.category.id, p.category.name) from Product p"
)
@NamedQuery(
        name = "Product.updateProductStatus",
        query = "update Product p set p.status=:status where p.id=:id"
)
@NamedQuery(
        name = "Product.getProductByCategory",
        query = "select new com.cafe.wrapper.ProductWrapper(p.id, p.name) from Product p where p.category.id=:id and p.status='true'"
)
@NamedQuery(
        name = "Product.getProductById",
        query = "select new com.cafe.wrapper.ProductWrapper(p.id, p.name, p.description, p.prize) from Product p where p.id=:id and p.status='true'"
)

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_jk", nullable = false)
    private Category category;
    private String description;
    private String prize;
    private String status;
}
