package ru.cherniak.menuvotingsystem.web.restaurant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.cherniak.menuvotingsystem.model.Restaurant;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminRestaurantRestController.REST_ADMIN_RESTAURANTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantRestController extends AbstractRestaurantController {

    static final String REST_ADMIN_RESTAURANTS = "rest/admin/restaurants";

    @Override
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable long id) {
        return super.get(id);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@RequestBody Restaurant restaurant) {
        Restaurant created = super.create(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_ADMIN_RESTAURANTS + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Restaurant update(@RequestBody Restaurant restaurant, @PathVariable long id) {
        return super.update(restaurant, id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        super.delete(id);
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
