package com.example.carrestservice.entity;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "cars")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Car implements Serializable {

    @Id
    @Column(name = "car_id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long carId;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @EqualsAndHashCode.Exclude
    @Column(name = "manufacture_year")
    private int manufactureYear;

    @OneToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "car_model_id", referencedColumnName = "car_model_id")
    private CarModel carModel;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "category_id")
    private Category category;

    public Car(Manufacturer manufacturer, int manufactureYear, CarModel carModel, Category category) {
        this.manufacturer = manufacturer;
        this.manufactureYear = manufactureYear;
        this.carModel = carModel;
        this.category = category;
    }
}
