package ru.cherniak.menuvotingsystem.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cherniak.menuvotingsystem.service.RestaurantService;
import ru.cherniak.menuvotingsystem.to.RestaurantTo;
import ru.cherniak.menuvotingsystem.util.RestaurantUtil;

import java.util.List;

@RestController
@RequestMapping(value = UserRestaurantRestController.REST_USER_RESTAURANTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantRestController {

    static final String REST_USER_RESTAURANTS = "rest/user/restaurants";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    RestaurantService restaurantService;

    @GetMapping("/{id}")
    public RestaurantTo getWithCountVotes(@PathVariable long id) {
        log.info("getWithListVotes {}", id);
        return RestaurantUtil.createTo(restaurantService.getWithVotes(id));
    }

    @GetMapping("/by")
    public RestaurantTo getByNameWithCountVotes(@RequestParam String name) {
        log.info("getByName {}", name);
        return RestaurantUtil.createTo(restaurantService.getByNameWithVotes(name));
    }

    @GetMapping
    public List<RestaurantTo> getAllWithVotes() {
        log.info("findAllWithVotes");
        RestaurantUtil.getRestaurantTosSortedByCountVotes(restaurantService.getAllWithVotes());
        return RestaurantUtil.getRestaurantTosSortedByCountVotes(restaurantService.getAllWithVotes());
    }
}

