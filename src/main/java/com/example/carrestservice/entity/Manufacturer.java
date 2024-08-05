package com.example.carrestservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "manufacturers")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Manufacturer implements Serializable {

    @Id
    @Column(name = "manufacturer_id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long manufacturerId;

    @EqualsAndHashCode.Exclude
    @Column(name = "manufacturer_name", unique = true)
    private String manufacturerName;

    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.REMOVE)
    private Set<Car> cars;
    public Manufacturer(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

}