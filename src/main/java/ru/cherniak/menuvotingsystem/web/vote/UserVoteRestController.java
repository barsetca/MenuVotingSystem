package ru.cherniak.menuvotingsystem.web.vote;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.cherniak.menuvotingsystem.model.Vote;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = UserVoteRestController.REST_USER_VOTES, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserVoteRestController extends AbstractVoteController {

    protected static final String REST_USER_VOTES = "rest/user/votes";

    @PostMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createUpdateWithLocation(@RequestBody Vote vote, @PathVariable long restaurantId) {
        Vote created = super.save(vote, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_USER_VOTES + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        super.delete();
    }

    @Override
    @GetMapping
    public List<Vote> getAllWithRestaurant() {
        return super.getAllWithRestaurant();
    }

    @Override
    @GetMapping(value = "/filter")
    public List<Vote> getAllWithRestaurantByUserIdBetween(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return super.getAllWithRestaurantByUserIdBetween(startDate, endDate);
    }
}
