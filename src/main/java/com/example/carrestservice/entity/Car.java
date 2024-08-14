package com.example.carrestservice.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@Table(name = "cars")
@NoArgsConstructor
@AllArgsConstructor
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


}
