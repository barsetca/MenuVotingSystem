package ru.cherniak.menuvotingsystem.web.user;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.web.AbstractDishController;

import java.util.List;

@RestController
@RequestMapping(value = UserDishRestController.REST_USER_DISHES, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserDishRestController extends AbstractDishController {

    static final String REST_USER_DISHES = "/rest/user/restaurants/{restaurantId}/dishes";

    @Override
    @GetMapping()
    public List<Dish> getTodayMenu(@PathVariable long restaurantId) {
        return super.getTodayMenu(restaurantId);
    }
}
