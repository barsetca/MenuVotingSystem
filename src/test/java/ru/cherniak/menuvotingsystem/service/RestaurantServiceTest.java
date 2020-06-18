package ru.cherniak.menuvotingsystem.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.cherniak.menuvotingsystem.DishTestData.*;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.*;


class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService service;

    @Autowired
    private DishService dishService;

    @Test
    void get() {
        Restaurant restaurant = service.get(RESTAURANT1_ID);
        RESTAURANT_MATCHER.assertMatch(restaurant, RESTAURANT1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void create() {
        Restaurant newRestaurant = new Restaurant("CreateRest", "пл. Новая, д.1", "315-00-00");
        service.create(newRestaurant);
        RESTAURANT_MATCHER.assertMatch(service.getAll(), newRestaurant, RESTAURANT1, RESTAURANT2);
    }

    @Test
    void duplicateNameCreate() {
        assertThrows(DataIntegrityViolationException.class,
                () -> service.create(new Restaurant("McDonalds", "пл. Новая, д.1", "315-00-00")));
    }

    @Test
    void update() {
        Restaurant updated = new Restaurant(RESTAURANT2);
        updated.setName("updateName");
        service.update(updated);
        RESTAURANT_MATCHER.assertMatch(service.get(RESTAURANT2_ID), updated);
    }

    @Test
    void updateNotFound() {
        Restaurant updated = new Restaurant(RESTAURANT2);
        updated.setName("updateName");
        updated.setId(1L);
        assertThrows(NotFoundException.class, () ->
                service.update(updated));
    }

    @Test
    void delete() {
        service.delete(RESTAURANT1_ID);
        RESTAURANT_MATCHER.assertMatch(service.getAll(), RESTAURANT2);
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void getByName() {
        RESTAURANT_MATCHER.assertMatch(service.getByName("McDonalds"), RESTAURANT1);
    }

    @Test
    void getByNameNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.getByName("Donalds"));
    }

    @Test
    void getAll() {
        List<Restaurant> all = service.getAll();
        RESTAURANT_MATCHER.assertMatch(all, RESTAURANT1, RESTAURANT2);
    }


    @Test
    void getWithDishes() {
        Restaurant restaurant = service.getWithDishes(RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(restaurant.getDishes(), ALL_DISHES_R1);
    }

    @Test
    void getWithDishesNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.getWithDishes(1));
    }

    @Test
    void getAllWithDishes() {
        List<Restaurant> restaurants = service.getAllWithDishes();
        Set<Dish> dishes1 = restaurants.get(0).getDishes();
        Set<Dish> dishes2 = restaurants.get(1).getDishes();
        DISH_MATCHER.assertMatch(dishes1, ALL_DISHES_R1);
        DISH_MATCHER.assertMatch(dishes2, ALL_DISHES_R2);
    }

    @Test
    void createWithValidationException() {
        validateRootCause(() -> service.create(new Restaurant("  ", "Veteranov avenue", "1234567")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant("M", "Veteranov avenue", "1234567")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant("MamaRoma", "  ", "1234567")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant("MamaRoma", "Vete", "1234567")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant("MamaRoma", "Veteranov avenue", "  ")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant("MamaRoma", "Veteranov avenue", "123")), ConstraintViolationException.class);

    }

    @Test
    void getAllWithDayMenu() {

        Dish dish1 = dishService.create(new Dish("Uno", 100), RESTAURANT1_ID);
        Dish dish2 = dishService.create(new Dish("Dos", 100), RESTAURANT1_ID);
        Dish dish3 = dishService.create(new Dish("Tres", 100), RESTAURANT1_ID);

        List<Restaurant> restaurants = service.getAllWithTodayMenu();
        Assert.assertEquals(restaurants.size(), 1);
        DISH_MATCHER.assertMatch(restaurants.get(0).getDishes().stream()
                .sorted(DISH_COMPARATOR).collect(Collectors.toList()), dish2, dish3, dish1);

    }
}