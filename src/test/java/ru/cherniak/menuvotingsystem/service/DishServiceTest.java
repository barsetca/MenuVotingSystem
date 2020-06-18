package ru.cherniak.menuvotingsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.cherniak.menuvotingsystem.DishTestData.*;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.RESTAURANT1_ID;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.RESTAURANT2_ID;


class DishServiceTest extends AbstractServiceTest {

    @Autowired
    private DishService service;

    @Test
    void create() {
        Dish create = service.create(getCreatedToday(), RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(service.getTodayMenu(RESTAURANT1_ID), create);
    }

    @Test
    public void createNotOwner() {
        assertThrows(DataIntegrityViolationException.class, () ->
                service.create(getCreatedToday(), 1));
    }

    @Test
    void duplicateNameWithDateCreate() {
        Dish created = new Dish(null, "БигМак", LocalDate.now(), 500);
        service.create(created, RESTAURANT1_ID);
        Dish createdDuplicateName = new Dish(null, "БигМак", LocalDate.now(), 5000);
        assertThrows(DataIntegrityViolationException.class, () -> service.create(createdDuplicateName, RESTAURANT1_ID));
    }

    @Test
    void update() {
        Dish newDish = new Dish(null, "БигМак", LocalDate.now(), 500);
        Dish created = service.create(newDish, RESTAURANT1_ID);
        Dish updated = getUpdated(created);
        service.update(updated, RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(service.get(created.getId(), RESTAURANT1_ID), updated);
    }

    @Test
    void updateNotFound() {
        Dish newDish = new Dish(null, "БигМак", LocalDate.now(), 500);
        Dish created = service.create(newDish, RESTAURANT1_ID);
        Dish updated = getUpdated(created);
        updated.setId(1L);
        assertThrows(NotFoundException.class, () ->
                service.update(updated, RESTAURANT1_ID));
    }

    @Test
    void updateNotOwner() {
        Dish newDish = new Dish(null, "БигМак", LocalDate.now(), 500);
        Dish created = service.create(newDish, RESTAURANT1_ID);
        Dish updated = getUpdated(created);
        assertThrows(NotFoundException.class, () ->
                service.update(updated, 1));
    }

    @Test
    void delete() {
        service.delete(DISH_ID, RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(service.getDayMenu(DATE_290420, RESTAURANT1_ID), DISH_2);
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(1, RESTAURANT1_ID));
    }

    @Test
    void get() {
        Dish getDish1 = service.get(DISH_ID, RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(service.getDayMenu(DATE_290420, RESTAURANT1_ID), getDish1, DISH_2);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1, RESTAURANT1_ID));
    }

    @Test
    void getAllByRestaurant() {
        List<Dish> all1 = service.getAllByRestaurant(RESTAURANT1_ID);
        List<Dish> all2 = service.getAllByRestaurant(RESTAURANT2_ID);
        DISH_MATCHER.assertMatch(all1, ALL_DISHES_R1);
        DISH_MATCHER.assertMatch(all2, ALL_DISHES_R2);
    }

    @Test
    void getDayMenu() {
        List<Dish> allByDay = service.getDayMenu(DATE_290420, RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(allByDay, DISH_1, DISH_2);
    }

    @Test
    void getTodayMenu() {
        Dish today = service.create(getCreatedToday(), RESTAURANT1_ID);
        List<Dish> allToday = service.getTodayMenu(RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(allToday, today);
    }

    @Test
    void getAllBetweenDatesInclusive() {
        Dish today = service.create(getCreatedToday(), RESTAURANT1_ID);
        List<Dish> allBetween = service.getAllByRestaurantBetweenDatesInclusive(DATE_300420, LocalDate.now(),
                RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(allBetween, today, DISH_5, DISH_6);
    }

    @Test
    void getAllBetweenNull() {
        Dish today = service.create(getCreatedToday(), RESTAURANT1_ID);

        List<Dish> allBetweenWithoutEnd = service.getAllByRestaurantBetweenDatesInclusive(DATE_300420, null,
                RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(allBetweenWithoutEnd, today, DISH_5, DISH_6);

        List<Dish> allBetweenWithoutStart = service.getAllByRestaurantBetweenDatesInclusive(null, DATE_300420,
                RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(allBetweenWithoutStart, DISH_5, DISH_6, DISH_1, DISH_2);

        List<Dish> allBetweenNulls = service.getAllByRestaurantBetweenDatesInclusive(null, null,
                RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(allBetweenNulls, today, DISH_5, DISH_6, DISH_1, DISH_2);

    }

    @Test
    void createWithValidationException() {
        validateRootCause(() -> service.create(new Dish(null, " ", LocalDate.now(), 1000), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Dish(null, "С", LocalDate.now(), 1000), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Dish(null, "Супчик", LocalDate.now(), -1), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Dish(null, "Супчик", of(2020, Month.APRIL, 30), 1000), RESTAURANT1_ID), ConstraintViolationException.class);
    }
}
