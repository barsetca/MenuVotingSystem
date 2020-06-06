package ru.cherniak.menuvotingsystem.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.cherniak.menuvotingsystem.AuthorizedUser;
import ru.cherniak.menuvotingsystem.View;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.to.UserTo;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(ProfileUserRestController.REST_URL)
public class ProfileUserRestController extends AbstractUserController {
    static final String REST_URL = "/rest/profile";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@AuthenticationPrincipal AuthorizedUser authUser) {
        return super.get(authUser.getId());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserTo userTo, @AuthenticationPrincipal AuthorizedUser authUser){
        super.updateTo(userTo, authUser.getId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
      User created = super.createTo(userTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authUser) {
        super.delete(authUser.getId());
    }

//    create!!!
}
