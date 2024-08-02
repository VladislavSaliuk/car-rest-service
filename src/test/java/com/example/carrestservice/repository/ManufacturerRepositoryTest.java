package com.example.carrestservice.repository;


import com.example.carrestservice.entity.Category;
import com.example.carrestservice.entity.Manufacturer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ManufacturerRepositoryTest {

    @Autowired
    ManufacturerRepository manufacturerRepository;

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_manufacturers.sql"})
    void save_shouldSaveChanges() {
        Manufacturer manufacturer = new Manufacturer("Test manufacturer");
        manufacturerRepository.save(manufacturer);
        assertEquals(11, manufacturerRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_manufacturers.sql"})
    void findAll_shouldReturnManufacturerList() {
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertFalse(manufacturerList.isEmpty());
        assertEquals(10, manufacturerList.size());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_manufacturers.sql"})
    void deleteById_shouldDeleteManufacturerFromDatabase_whenInputContainsExistingManufacturerId() {
        long manufacturerId = 5;
        manufacturerRepository.deleteById(manufacturerId);
        assertEquals(9, manufacturerRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_manufacturers.sql"})
    void deleteById_shouldNotDeleteManufacturerFromDatabase_whenInputContainsNotExistingManufacturerId() {
        long manufacturerId =100;
        manufacturerRepository.deleteById(manufacturerId);
        assertEquals(10, manufacturerRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_manufacturers.sql"})
    void findManufacturerByManufacturerId_shouldReturnManufacturer_whenInputContainsExistingManufacturerId() {

        long manufacturerId = 3;
        String manufacturerName = "Honda";

        Manufacturer expectedManufacturer = new Manufacturer();

        expectedManufacturer.setManufacturerId(manufacturerId);
        expectedManufacturer.setManufacturerName(manufacturerName);
        expectedManufacturer.setCars(Collections.emptySet());

        Optional<Manufacturer> actualManufacturer = manufacturerRepository.findById(manufacturerId);

        assertTrue(actualManufacturer.isPresent());
        assertEquals(expectedManufacturer, actualManufacturer.get());

    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_manufacturers.sql"})
    void findById_shouldReturnNull_whenInputContainsNotExistingManufacturerId() {
        long manufacturerId = 100;
        Optional<Manufacturer> expectedManufacturer = manufacturerRepository.findById(manufacturerId);
        assertTrue(expectedManufacturer.isEmpty());
    }

}
