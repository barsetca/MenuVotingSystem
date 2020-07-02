package ru.cherniak.menuvotingsystem.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping(value = UserRestaurantRestController.REST_USER_RESTAURANTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class
UserRestaurantRestController {

    static final String REST_USER_RESTAURANTS = "/rest/user/restaurants";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    RestaurantService restaurantService;

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable long id) {
        log.info("get {}", id);
        return restaurantService.get(id);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantService.getAll();
    }

    @GetMapping("/byName")
    public Restaurant getByName(@RequestParam String name) {
        log.info("getByName {}", name);
        return restaurantService.getByName(name);
    }

    @GetMapping("dishes/today")
    public List<Restaurant> getAllWithTodayMenu() {
        log.info("getAllWithTodayMenu");
        return restaurantService.getAllWithTodayMenu();
    }


}

