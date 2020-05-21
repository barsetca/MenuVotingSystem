package ru.cherniak.menuvotingsystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import ru.cherniak.menuvotingsystem.DishTestData;
import ru.cherniak.menuvotingsystem.VoteTestData;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.model.Vote;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.*;


class RestaurantServiceTest extends AbstractServiceTest{

    @Autowired
    private RestaurantService service;

    @Test
   void get() {
        Restaurant restaurant = service.get(RESTAURANT1_ID);
        assertMatch(restaurant, RESTAURANT1);
    }


    @Test
    void create() {
        Restaurant newRestaurant = new Restaurant("CreateRest", "пл. Новая, д.1", "315-00-00");
        service.create(newRestaurant);
        assertMatch(service.getAll(), newRestaurant, RESTAURANT1, RESTAURANT2);
    }

    @Test
   void duplicateNameCreate() throws Exception {
        assertThrows(DataIntegrityViolationException.class,
                () -> service.create(new Restaurant("McDonalds", "пл. Новая, д.1", "315-00-00")));
    }

    @Test
    void update() {
        Restaurant updated = new Restaurant(RESTAURANT2);
        updated.setName("updateName");
        service.update(updated);
        assertMatch(service.get(RESTAURANT2_ID), updated);
    }

    @Test
    void delete() {
        service.delete(RESTAURANT1_ID);
        assertMatch(service.getAll(), RESTAURANT2);
    }

    @Test
    void getByName() {
        assertMatch(service.getByName("McDonalds"), RESTAURANT1);
    }

    @Test
    void getAll() {
        List<Restaurant> all = service.getAll();
        assertMatch(all, RESTAURANT1, RESTAURANT2);
    }

    @Test
    void getWithListVotes() {
        Restaurant restaurant = service.getWithListVotes(RESTAURANT2_ID);
        VoteTestData.assertMatch(restaurant.getVotes(), VoteTestData.VOTE_3, VoteTestData.VOTE_2);
    }

    @Test
    void getWithListDishes() {
        Restaurant restaurant = service.getWithListDishes(RESTAURANT1_ID);
        DishTestData.assertMatch(restaurant.getDishes(), DishTestData.ALL_DISHES_R1);
    }

    @Test
    void findAllWithDishes() {
        List<Restaurant> restaurants = service.getAllWithDishes();
        Set<Dish> dishes1 = restaurants.get(0).getDishes();
        Set<Dish> dishes2 = restaurants.get(1).getDishes();
        DishTestData.assertMatch(dishes1, DishTestData.ALL_DISHES_R1);
        DishTestData.assertMatch(dishes2, DishTestData.ALL_DISHES_R2);

    }

    @Test
    void findAllWithVotes() {
        List<Restaurant> restaurants = service.getAllWithVotes();
        Set<Vote> votes1 = restaurants.get(0).getVotes();
        Set<Vote> votes2 = restaurants.get(1).getVotes();
        VoteTestData.assertMatch(votes1, VoteTestData.VOTE_1);
        VoteTestData.assertMatch(votes2, VoteTestData.VOTE_3, VoteTestData.VOTE_2);

    }

    @Test
    void getWithDishesAndVotes() {
        List<Restaurant> restaurants = service.getAllWithDishesAndVotes();
        Set<Dish> dishes1 = restaurants.get(0).getDishes();
        Set<Dish> dishes2 = restaurants.get(1).getDishes();
        Set<Vote> votes1 = restaurants.get(0).getVotes();
        Set<Vote> votes2 = restaurants.get(1).getVotes();
        DishTestData.assertMatch(dishes1, DishTestData.ALL_DISHES_R1);
        DishTestData.assertMatch(dishes2, DishTestData.ALL_DISHES_R2);
        VoteTestData.assertMatch(votes1, VoteTestData.VOTE_1);
        VoteTestData.assertMatch(votes2, VoteTestData.VOTE_3, VoteTestData.VOTE_2);
    }

}