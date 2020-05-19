package ru.cherniak.menuvotingsystem.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.cherniak.menuvotingsystem.DishTestData;
import ru.cherniak.menuvotingsystem.VoteTestData;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.model.Vote;

import java.util.ArrayList;
import java.util.List;

import static ru.cherniak.menuvotingsystem.RestaurantTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class RestaurantServiceTest {

    @Autowired
    private RestaurantService service;

    @Test
    public void get() {
        Restaurant restaurant = service.get(RESTAURANT1_ID);
        assertMatch(restaurant, RESTAURANT1);
    }


    @Test
    public void create() {
        Restaurant newRestaurant = new Restaurant("CreateRest", "пл. Новая, д.1", "315-00-00");
        service.create(newRestaurant);
        assertMatch(service.getAll(), newRestaurant, RESTAURANT1, RESTAURANT2);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void duplicateNameCreate() throws Exception {
        service.create(new Restaurant("McDonalds", "пл. Новая, д.1", "315-00-00"));
    }

    @Test
    public void update() {
        Restaurant updated = new Restaurant(RESTAURANT2);
        updated.setName("updateName");
        service.update(updated);
        assertMatch(service.get(RESTAURANT2_ID), updated);
    }

    @Test
    public void delete() {
        service.delete(RESTAURANT1_ID);
        assertMatch(service.getAll(), RESTAURANT2);
    }

    @Test
    public void getByName() {
        assertMatch(service.getByName("McDonalds"), RESTAURANT1);
    }

    @Test
    public void getAll() {
        List<Restaurant> all = service.getAll();
        assertMatch(all, RESTAURANT1, RESTAURANT2);
    }

    @Test
    public void getWithListVotes() {
        Restaurant restaurant = service.getWithListVotes(RESTAURANT2_ID);
        VoteTestData.assertMatch(restaurant.getVotes(), VoteTestData.VOTE_3, VoteTestData.VOTE_2);
    }

    @Test
    public void getWithListDishes() {
        Restaurant restaurant = service.getWithListDishes(RESTAURANT1_ID);
        DishTestData.assertMatch(restaurant.getDishes(), DishTestData.ALL_DISHES_R1);
    }

    @Test
    public void findAllWithDishes() {
        List<Restaurant> restaurants = service.findAllWithDishes();
        List<Dish> dishes1 = restaurants.get(0).getDishes();
        List<Dish> dishes2 = restaurants.get(1).getDishes();
        DishTestData.assertMatch(dishes1, DishTestData.ALL_DISHES_R1);
        DishTestData.assertMatch(dishes2, DishTestData.ALL_DISHES_R2);

    }

    @Test
    public void findAllWithVotes() {
        List<Restaurant> restaurants = service.findAllWithVotes();
        List<Vote> votes1 = restaurants.get(0).getVotes();
        List<Vote> votes2 = restaurants.get(1).getVotes();
        VoteTestData.assertMatch(votes1, VoteTestData.VOTE_1);
        VoteTestData.assertMatch(votes2, VoteTestData.VOTE_3, VoteTestData.VOTE_2);

    }

}