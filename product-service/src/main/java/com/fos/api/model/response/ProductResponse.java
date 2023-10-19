package com.fos.api.model.response;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ProductResponse {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String name;
    private String description;
    private Double price;
    private String status;


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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                ", productName='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}
