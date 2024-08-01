package com.example.carrestservice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "categories")
@NoArgsConstructor
public class Category implements Serializable {

    @Id
    @Column(name= "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;

    @Column(name = "category_name", unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private Set<Car> cars;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

}