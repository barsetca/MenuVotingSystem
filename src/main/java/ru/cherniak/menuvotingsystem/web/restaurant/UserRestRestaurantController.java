package ru.cherniak.menuvotingsystem.web.restaurant;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cherniak.menuvotingsystem.model.Restaurant;

import java.util.List;

@RestController
@RequestMapping(value = UserRestRestaurantController.REST_USER_RESTAURANTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestRestaurantController extends AbstractRestaurantController {

    static final String REST_USER_RESTAURANTS = "rest/user/restaurants";

    @Override
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable long id) {
        return super.get(id);
    }

    @Override
    @GetMapping("/by")
    public Restaurant getByName(@RequestParam String name) {
        return super.getByName(name);
    }

    @Override
    @GetMapping
    public List<Restaurant> getAll() {
        return super.getAll();
    }
}
