package ru.cherniak.menuvotingsystem.web.dish;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cherniak.menuvotingsystem.model.Dish;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = UserDishRestController.REST_USER_DISHES, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserDishRestController extends AbstractDishController {

    static final String REST_USER_DISHES = "rest/user/dishes";


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
    public List<Dish> getAllWithRestaurant(){
        return super.getAllWithRestaurant();
    }
}
