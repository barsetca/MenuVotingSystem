package ru.cherniak.menuvotingsystem.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.service.RestaurantService;

import java.net.URI;
import java.util.List;

import static ru.cherniak.menuvotingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantRestController.REST_ADMIN_RESTAURANTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantRestController {

    static final String REST_ADMIN_RESTAURANTS = "rest/admin/restaurants";

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

    @GetMapping("/by")
    public Restaurant getByName(@RequestParam String name) {
        log.info("getByName {}", name);
        return restaurantService.getByName(name);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        restaurantService.create(restaurant);
        Restaurant created = restaurantService.create(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_ADMIN_RESTAURANTS + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Restaurant update(@RequestBody Restaurant restaurant, @PathVariable long id) {
        log.info("update {}", restaurant);
        assureIdConsistent(restaurant, id);
        return restaurantService.update(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("delete {}", id);
        restaurantService.delete(id);
    }
}
