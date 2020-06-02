package ru.cherniak.menuvotingsystem.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.cherniak.menuvotingsystem.service.DishService;
import ru.cherniak.menuvotingsystem.service.RestaurantService;
import ru.cherniak.menuvotingsystem.service.UserService;
import ru.cherniak.menuvotingsystem.service.VoteService;

import javax.servlet.http.HttpServletRequest;
import java.time.Month;

import static java.time.LocalDate.of;

@Controller
public class RootController {

//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private RestaurantService restaurantService;
//
//    @Autowired
//    private DishService dishService;
//
//    @Autowired
//    private VoteService voteService;

    @GetMapping()
    public String root() {
        return "index";
    }

//    @GetMapping("users")
//    public String getUsers(Model model) {
//        model.addAttribute("users", userService.getAll());
//        return "users";
//    }
//
//    @GetMapping("restaurants")
//    public String getRestaurants(Model model) {
//        model.addAttribute("restaurants", restaurantService.getAll());
//        return "restaurants";
//    }
//
//    @GetMapping("dishes")
//    public String getDishes(Model model) {
//        model.addAttribute("dishes", dishService.getAllWithRestaurant());
//        return "dishes";
//    }
//
//    @GetMapping("votes")
//    public String getVotes(Model model) {
//        model.addAttribute("votes", voteService.getAllByUserIdWithRestaurant(SecurityUtil.authUserId()));
//        return "votes";
//    }
//
//    @GetMapping("rating")
//    public String getRating(Model model) {
//        model.addAttribute("ratings", restaurantService.getAllWithVotes());
//        return "ratings";
//    }
//
////    @PostMapping("users")
////    public String setUser(HttpServletRequest request) {
////        long userId = Integer.parseInt(request.getParameter("userId"));
////        SecurityUtil.setAuthUserId(userId);
////        return "redirect:rating";
////    }

}
