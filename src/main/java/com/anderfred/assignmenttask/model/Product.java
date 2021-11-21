package com.anderfred.assignmenttask.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(indexName = "productindex")
public class Product implements Serializable {
    private static final long serialVersionUID = -4951168671068568517L;

    public Product(Double price, String name, String sku) {
        this.price = price;
        this.name = name;
        this.sku = sku;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @org.springframework.data.annotation.Id
    @Field(type = FieldType.Long, name = "id")
    private Long id;

    @Column(name = "price", nullable = false)
    @Field(type = FieldType.Double, name = "price")
    private Double price;

    @Column(name = "name", nullable = false)
    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Column(name = "sku", nullable = false)
    @Field(type = FieldType.Text, name = "sku")
    private String sku;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && price.equals(product.price) && name.equals(product.name) && sku.equals(product.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, name, sku);
    }
}
