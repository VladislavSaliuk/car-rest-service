package com.example.carrestservice.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "categories")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category implements Serializable {

    @Id
    @Column(name= "category_id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_generator")
    @SequenceGenerator(name = "category_id_generator", initialValue = 1, allocationSize = 1, sequenceName = "category_id_seq")
    private long categoryId;

    @EqualsAndHashCode.Exclude
    @Column(name = "category_name", unique = true)
    private String categoryName;

    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private Set<Car> cars;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

}