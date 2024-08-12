package com.example.carrestservice.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_id_generator")
    @SequenceGenerator(name = "car_id_generator", initialValue = 1, allocationSize = 1, sequenceName = "car_id_seq")
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

    public Car(CarBuilder carBuilder) {
        this.carId = carBuilder.carId;
        this.manufacturer = carBuilder.manufacturer;
        this.manufactureYear = carBuilder.manufactureYear;
        this.carModel = carBuilder.carModel;
        this.category = carBuilder.category;
    }

    public static class CarBuilder {
        private long carId;
        private Manufacturer manufacturer;
        private int manufactureYear;
        private CarModel carModel;
        private Category category;

        public CarBuilder carId(long carId) {
            this.carId = carId;
            return this;
        }

        public CarBuilder manufacturer(Manufacturer manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public CarBuilder manufactureYear(int manufactureYear) {
            this.manufactureYear = manufactureYear;
            return this;
        }

        public CarBuilder carModel(CarModel carModel) {
            this.carModel = carModel;
            return this;
        }

        public CarBuilder category(Category category) {
            this.category = category;
            return this;
        }

        public Car build() {
            return new Car(this);
        }

    }


}
