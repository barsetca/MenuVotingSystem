package ru.cherniak.menuvotingsystem.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cherniak.menuvotingsystem.model.User;

import static ru.cherniak.menuvotingsystem.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(ProfileUserRestController.REST_URL)
public class ProfileUserRestController extends AbstractUserController {
    static final String REST_URL = "rest/profile";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get() {
        return super.get(authUserId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        super.delete(authUserId());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user) {
        super.update(user, authUserId());
    }
}
