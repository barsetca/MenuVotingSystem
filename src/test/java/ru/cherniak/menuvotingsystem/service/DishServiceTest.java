package ru.cherniak.menuvotingsystem.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.cherniak.menuvotingsystem.RestaurantTestData;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

import static ru.cherniak.menuvotingsystem.DishTestData.*;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.RESTAURANT1;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.RESTAURANT1_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class DishServiceTest {

    @Autowired
    private DishService service;

    @Test
    public void create() {
        Dish create = service.create(getCreatedToday(), RESTAURANT1_ID);
        assertMatch(service.getTodayMenu(RESTAURANT1_ID), create);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void duplicateNameWithDateCreate() {
        Dish dish = new Dish(null, "БигМак", DATE_300520, 500);
        service.create(dish, RESTAURANT1_ID);
    }

    @Test
    public void update() {
        Dish updated = getUpdated();
        service.update(updated, RESTAURANT1_ID);
        assertMatch(service.get(DISH_ID, RESTAURANT1_ID), updated);
    }

    @Test
    public void delete() {
        service.delete(DISH_ID, RESTAURANT1_ID);
        assertMatch(service.getDayMenu(DATE_300520, RESTAURANT1_ID), DISH_2);
    }

    @Test
    public void get() {
        Dish getDish1 = service.get(DISH_ID, RESTAURANT1_ID);
        assertMatch(service.getDayMenu(DATE_300520, RESTAURANT1_ID), getDish1, DISH_2);
    }

    @Test
    public void getAll() {
        List<Dish> all1 = service.getAll(RESTAURANT1_ID);
        List<Dish> all2 = service.getAll(RestaurantTestData.RESTAURANT2_ID);
        assertMatch(all1, ALL_DISHES_R1);
        assertMatch(all2, ALL_DISHES_R2);
    }

    @Test
    public void getDayMenu() {
        List<Dish> allByDay = service.getDayMenu(DATE_300520, RESTAURANT1_ID);
        assertMatch(allByDay, DISH_1, DISH_2);
    }

    @Test
    public void getTodayMenu() {
        Dish today = service.create(getCreatedToday(), RESTAURANT1_ID);
        List<Dish> allToday = service.getTodayMenu(RESTAURANT1_ID);
        assertMatch(allToday, today);
    }

    @Test
    public void getAllBetweenDatesInclusive() {
        Dish today = service.create(getCreatedToday(), RESTAURANT1_ID);
        List<Dish> allBetween = service.getAllBetweenDatesInclusive(LocalDate.now(), DATE_300520,
                RESTAURANT1_ID);
        assertMatch(allBetween, DISH_1, DISH_2, today);
    }

    @Test
    public void getDayMenuByDateWithRestaurant() {
        List<Dish> dishes = service.getDayMenuByDateWithRestaurant(DATE_300520, RESTAURANT1_ID);
        Restaurant restaurant = dishes.get(0).getRestaurant();
        RestaurantTestData.assertMatch(restaurant, RESTAURANT1);

    }

    @Test
    public void getWithRestaurant() {
        Dish dish = service.getWithRestaurant(DISH_ID, RESTAURANT1_ID);
        Restaurant restaurant = dish.getRestaurant();
        RestaurantTestData.assertMatch(restaurant, RESTAURANT1);

    }
}