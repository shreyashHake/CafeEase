package com.cafe.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWrapper {
    Integer id;
    String name;
    String description;
    String price;
    String status;
    Integer categoryId;
    String categoryName;

    public ProductWrapper(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public ProductWrapper(Integer id, String name, String description, String price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
