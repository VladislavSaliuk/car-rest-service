package com.example.carrestservice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "cars")
@NoArgsConstructor
public class Car implements Serializable {

    @Id
    @Column(name = "car_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long carId;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id",nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "manufacture_year")
    private int manufactureYear;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_model_id", referencedColumnName = "car_model_id")
    private CarModel carModel;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
