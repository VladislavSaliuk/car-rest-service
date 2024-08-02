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
@Table(name = "car_models")
@NoArgsConstructor
@EqualsAndHashCode
public class CarModel implements Serializable {

    @Id
    @Column(name = "car_model_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long carModelId;

    @Column(name = "car_model_name", unique = true, nullable = false)
    private String carModelName;

    @OneToOne(mappedBy = "carModel")
    private Car car;

    public CarModel(String carModelName) {
        this.carModelName = carModelName;
    }
}
