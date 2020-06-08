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

    @GetMapping()
    public String root() {
        return "index";
    }

}
