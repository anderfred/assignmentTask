package com.anderfred.assignmenttask.model;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(indexName = "orderitemindex")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 5943057001293881561L;

    public OrderItem(Long quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Field(type = FieldType.Long, name = "id")
    private Long id;

    @Column(name = "quantity", nullable = false)
    @Field(type = FieldType.Double, name = "quantity")
    private Long quantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    @Field(type = FieldType.Nested, includeInParent = true)
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) && quantity.equals(orderItem.quantity) && Objects.equals(product, orderItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, product);
    }
}
