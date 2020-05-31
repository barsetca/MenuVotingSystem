package ru.cherniak.menuvotingsystem.web.dish;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cherniak.menuvotingsystem.model.Dish;

import java.util.List;

@RestController
@RequestMapping(value = UserDishRestController.REST_USER_DISHES, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserDishRestController extends AbstractDishController {

    static final String REST_USER_DISHES = "rest/user/dishes";

    @Override
    @GetMapping("/by")
    public List<Dish> getTodayMenu(@RequestParam long restaurantId) {
        return super.getTodayMenu(restaurantId);
    }
}
