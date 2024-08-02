package com.example.carrestservice.entity;

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
@EqualsAndHashCode
public class Manufacturer implements Serializable {

    @Id
    @Column(name = "manufacturer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long manufacturerId;

    @Column(name = "manufacturer_name", unique = true, nullable = false)
    private String manufacturerName;

    @OneToMany(mappedBy = "manufacturer")
    private Set<Car> cars;
    public Manufacturer(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

}