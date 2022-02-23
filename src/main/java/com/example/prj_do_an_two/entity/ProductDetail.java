package com.example.prj_do_an_two.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "product_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String name;
    @Column(nullable = false, length = 255)
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductDetail(String name, String value, Product product) {
        this.name = name;
        this.value = value;
        this.product = product;
    }
}
