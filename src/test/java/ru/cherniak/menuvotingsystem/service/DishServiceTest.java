package ru.cherniak.menuvotingsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.cherniak.menuvotingsystem.RestaurantTestData;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.cherniak.menuvotingsystem.DishTestData.assertMatch;
import static ru.cherniak.menuvotingsystem.DishTestData.*;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.*;


class DishServiceTest extends AbstractServiceTest {

    @Autowired
    private DishService service;

    @Test
    void create() {
        Dish create = service.create(getCreatedToday(), RESTAURANT1_ID);
        assertMatch(service.getTodayMenu(RESTAURANT1_ID), create);
    }

    @Test
    void duplicateNameWithDateCreate() {
        Dish dish = new Dish(null, "БигМак", DATE_300520, 500);
        assertThrows(DataIntegrityViolationException.class, () -> service.create(dish, RESTAURANT1_ID));
    }

    @Test
    void update() {
        Dish updated = getUpdated();
        service.update(updated, RESTAURANT1_ID);
        assertMatch(service.get(DISH_ID), updated);
    }

    @Test
    void delete() {
        service.delete(DISH_ID);
        assertMatch(service.getDayMenu(DATE_300520, RESTAURANT1_ID), DISH_2);
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));

    }

    @Test
    void get() {
        Dish getDish1 = service.get(DISH_ID);
        assertMatch(service.getDayMenu(DATE_300520, RESTAURANT1_ID), getDish1, DISH_2);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getAllByRestaurant() {
        List<Dish> all1 = service.getAllByRestaurant(RESTAURANT1_ID);
        List<Dish> all2 = service.getAllByRestaurant(RESTAURANT2_ID);
        assertMatch(all1, ALL_DISHES_R1);
        assertMatch(all2, ALL_DISHES_R2);
    }

    @Test
    void getDayMenu() {
        List<Dish> allByDay = service.getDayMenu(DATE_300520, RESTAURANT1_ID);
        assertMatch(allByDay, DISH_1, DISH_2);
    }

    @Test
    void getTodayMenu() {
        Dish today = service.create(getCreatedToday(), RESTAURANT1_ID);
        List<Dish> allToday = service.getTodayMenu(RESTAURANT1_ID);
        assertMatch(allToday, today);
    }

    @Test
    void getAllBetweenDatesInclusive() {
        Dish today = service.create(getCreatedToday(), RESTAURANT1_ID);
        List<Dish> allBetween = service.getAllByRestaurantBetweenDatesInclusive(LocalDate.now(), DATE_300520,
                RESTAURANT1_ID);
        assertMatch(allBetween, DISH_1, DISH_2, today);
    }

    @Test
    void getDayMenuByDateWithRestaurant() {
        List<Dish> dishes = service.getDayMenuByDateWithRestaurant(DATE_300520, RESTAURANT1_ID);
        Restaurant restaurant = dishes.get(0).getRestaurant();
        assertMatch(dishes, DISH_1, DISH_2);
        RestaurantTestData.assertMatch(restaurant, RESTAURANT1);

    }

    @Test
    void getWithRestaurant() {
        Dish dish = service.getWithRestaurant(DISH_ID, RESTAURANT1_ID);
        Restaurant restaurant = dish.getRestaurant();
        assertMatch(dish, DISH_1);
        RestaurantTestData.assertMatch(restaurant, RESTAURANT1);

    }

    @Test
    public void getWithRestaurantNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.getWithRestaurant(DISH_ID, 1));
        assertThrows(NotFoundException.class, () ->
                service.getWithRestaurant(1, RESTAURANT1_ID));
    }


    @Test
    void getAllDayMenuByDateWithRestaurant() {
        List<Dish> dishes = service.getAllDayMenuByDateWithRestaurant(DATE_300520);
        List<Restaurant> restaurants = new ArrayList<>();
        dishes.forEach(d -> restaurants.add(d.getRestaurant()));
        assertMatch(dishes, DISH_1, DISH_2, DISH_4, DISH_3);
        RestaurantTestData.assertMatch(restaurants, RESTAURANT1, RESTAURANT1, RESTAURANT2, RESTAURANT2);
    }

    @Test
    void getAllWithRestaurant() {
        List<Dish> dishes = service.getAllWithRestaurant();
        List<Restaurant> restaurants = new ArrayList<>();
        dishes.forEach(d -> restaurants.add(d.getRestaurant()));
        assertMatch(dishes, ALL_DISHES);
        RestaurantTestData.assertMatch(restaurants,
                RESTAURANT1, RESTAURANT1, RESTAURANT2, RESTAURANT2,
                RESTAURANT1, RESTAURANT1, RESTAURANT2, RESTAURANT2);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(new Dish(null, " ", LocalDate.now(), 1000), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Dish(null, "С", LocalDate.now(), 1000), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Dish(null, "Супчик", LocalDate.now(), -1), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Dish(null, "Супчик", of(2020, Month.APRIL, 30), 1000), RESTAURANT1_ID), ConstraintViolationException.class);

    }
}
