package ru.cherniak.menuvotingsystem.web.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.cherniak.menuvotingsystem.View;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.web.AbstractDishController;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminDishRestController.REST_ADMIN_DISHES, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishRestController extends AbstractDishController {

    static final String REST_ADMIN_DISHES = "/rest/admin/restaurants/{restaurantId}/dishes";

    @Override
    @GetMapping("/{id}")
    public Dish get(@PathVariable long restaurantId, @PathVariable long id) {
        return super.get(id, restaurantId);
    }

    @Override
    @GetMapping()
    public List<Dish> getAllByRestaurant(@PathVariable long restaurantId) {
        return super.getAllByRestaurant(restaurantId);
    }

    @Override
    @GetMapping("/filter")
    public List<Dish> getAllByRestaurantBetweenDatesInclusive(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @PathVariable long restaurantId) {
        log.info("getAllByRestaurantBetweenDatesInclusive {} - {} of user {}", startDate, endDate, restaurantId);
        return super.getAllByRestaurantBetweenDatesInclusive(startDate, endDate, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Validated(View.Web.class) @RequestBody Dish dish, @PathVariable long restaurantId) {
        Dish created = super.create(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/rest/admin/restaurants/" + restaurantId + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody Dish dish, @PathVariable long restaurantId) {
        super.update(dish, restaurantId);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long restaurantId, @PathVariable long id) {
        super.delete(id, restaurantId);
    }
}
