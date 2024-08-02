package com.example.carrestservice.repository;

import com.example.carrestservice.entity.CarModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarModelRepositoryTest {

    @Autowired
    CarModelRepository carModelRepository;

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_car_models.sql"})
    void save_shouldSaveChanges_whenInputContainsCarModelWithNotExistingCarModelName() {
        CarModel carModel = new CarModel("Test car model");
        carModelRepository.save(carModel);
        assertEquals(11, carModelRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_car_models.sql"})
    void save_shouldThrowException_whenInputContainsCarModelWithExistingCarModelName() {
        CarModel carModel = new CarModel("Civic");
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> carModelRepository.save(carModel));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_car_models.sql"})
    void save_shouldThrowException_whenInputContainsCarModelWithNull() {
        CarModel carModel = new CarModel(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> carModelRepository.save(carModel));
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_car_models.sql"})
    void findAll_shouldReturnCarModelList() {
        List<CarModel> carModelList = carModelRepository.findAll();
        assertFalse(carModelList.isEmpty());
        assertEquals(10, carModelList.size());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_car_models.sql"})
    void deleteById_shouldDeleteCarModelFromDatabase_whenInputContainsExistingCarModelId() {
        long carModelId = 5;
        carModelRepository.deleteById(carModelId);
        assertEquals(9, carModelRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_car_models.sql"})
    void deleteById_shouldNotDeleteCarModelFromDatabase_whenInputContainsNotExistingCarModelId() {
        long carModelId =100;
        carModelRepository.deleteById(carModelId);
        assertEquals(10, carModelRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_car_models.sql"})
    void findById_shouldReturnCarModel_whenInputContainsExistingCarModelId() {

        long carModelId = 3;
        String carModelName = "Mustang";

        CarModel expectedCarModel = new CarModel();

        expectedCarModel.setCarModelId(carModelId);
        expectedCarModel.setCarModelName(carModelName);

        Optional<CarModel> actualCarModel = carModelRepository.findById(carModelId);

        assertTrue(actualCarModel.isPresent());
        assertEquals(expectedCarModel, actualCarModel.get());

    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_car_models.sql"})
    void findById_shouldReturnNull_whenInputContainsNotExistingCarModelId() {
        long carModelId = 100;
        Optional<CarModel> expectedCarModel = carModelRepository.findById(carModelId);
        assertTrue(expectedCarModel.isEmpty());
    }



}
