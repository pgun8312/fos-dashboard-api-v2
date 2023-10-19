package com.fos.api.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Product")
@NoArgsConstructor
@EqualsAndHashCode
public class Product {

    public Product(String name, String description, double price, String status) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
    }

    @Id
    @Column(name = "ID")
//    @SequenceGenerator(
//            name = "product_id_sequence",
//            sequenceName = "product_id_sequence",
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "product_id_sequence"
//    )

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "NAME")
    private String name;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="PRICE")
    private double price;

    @Column(name = "STATUS")
    private String status;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
