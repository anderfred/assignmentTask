package com.anderfred.assignmenttask.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "`order`")        //Wasted here 30 min of life
@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(indexName = "orderindex")
public class Order implements Serializable {
    private static final long serialVersionUID = 4345128850808228634L;

    public Order(Set<OrderItem> orderItems, Double totalAmount) {
        this.orderItems = orderItems;
        this.totalAmount = totalAmount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @org.springframework.data.annotation.Id
    @Field(type = FieldType.Long, name = "id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @Column(name = "total_amount", nullable = false)
    @Field(type = FieldType.Double, name = "total_amount")
    private Double totalAmount = 0d;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    @Field(type = FieldType.Date, name = "created_at")
    private Instant createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id) && Objects.equals(orderItems, order.orderItems) && totalAmount.equals(order.totalAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderItems, totalAmount);
    }
}
