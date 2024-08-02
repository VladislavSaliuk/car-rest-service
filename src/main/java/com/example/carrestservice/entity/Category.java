package com.example.carrestservice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "categories")
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Category implements Serializable {

    @Id
    @Column(name= "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;

    @Column(name = "category_name", unique = true, nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private Set<Car> cars;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

}