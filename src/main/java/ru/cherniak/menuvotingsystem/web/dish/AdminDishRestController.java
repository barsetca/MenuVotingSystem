package ru.cherniak.menuvotingsystem.web.dish;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.cherniak.menuvotingsystem.model.Dish;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminDishRestController.REST_ADMIN_DISHES, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishRestController extends AbstractDishController {

    static final String REST_ADMIN_DISHES = "rest/admin/dishes";


    @PostMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@RequestBody Dish dish, @PathVariable long restaurantId) {
        Dish created = super.create(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_ADMIN_DISHES + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);

    }

    @Override
    @PutMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Dish dish, @PathVariable long restaurantId) {
        super.update(dish, restaurantId);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean delete(@PathVariable long id) {
        return super.delete(id);
    }

    @Override
    @GetMapping("/{id}")
    public Dish get(@PathVariable long id) {
        return super.get(id);
    }

    @Override
    @GetMapping("/{restaurantId}/by")
    public List<Dish> getDayMenu(@RequestParam LocalDate date, @PathVariable long restaurantId) {
        return super.getDayMenu(date, restaurantId);
    }

    @Override
    @GetMapping("/by")
    public List<Dish> getTodayMenu(@RequestParam long restaurantId) {
        return super.getTodayMenu(restaurantId);
    }


    @Override
    @GetMapping
    public List<Dish> getAllWithRestaurant() {
        return super.getAllWithRestaurant();
    }
}