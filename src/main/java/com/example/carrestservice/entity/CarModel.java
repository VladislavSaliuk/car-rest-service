package com.example.carrestservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CarModel implements Serializable {

    @Id
    @Column(name = "car_model_id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_model_id_generator")
    @SequenceGenerator(name = "car_model_id_generator", initialValue = 1, allocationSize = 1, sequenceName = "car_model_id_seq")
    private long carModelId;

    @EqualsAndHashCode.Exclude
    @Column(name = "car_model_name", unique = true)
    private String carModelName;

    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "carModel", cascade = CascadeType.REMOVE)
    private Car car;

    public CarModel(String carModelName) {
        this.carModelName = carModelName;
    }
}
