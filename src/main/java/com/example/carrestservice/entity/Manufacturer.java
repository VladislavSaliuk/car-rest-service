package com.example.carrestservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manufacturer_id_generator")
    @SequenceGenerator(name = "manufacturer_id_generator", initialValue = 1, allocationSize = 1, sequenceName = "manufacturer_id_seq")
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