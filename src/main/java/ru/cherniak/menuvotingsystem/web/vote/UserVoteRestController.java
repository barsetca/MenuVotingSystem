package ru.cherniak.menuvotingsystem.web.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.service.VoteService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.cherniak.menuvotingsystem.web.SecurityUtil.authUserId;


@RestController
@RequestMapping(value = UserVoteRestController.REST_USER_VOTES, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserVoteRestController {

    protected static final String REST_USER_VOTES = "/rest/user/votes";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private VoteService voteService;

    @GetMapping("/{id}")
    public Vote get(@PathVariable long id) {
        long userId = authUserId();
        log.info("get {} by user {}", id, userId);
        return voteService.getWithRestaurant(id, userId);
    }

    @GetMapping()
    public List<Vote> getAllByUserIdWithRestaurant() {
        long userId = authUserId();
        log.info("getAllByUserIdWithRestaurant by user {}", userId);
        return voteService.getAllByUserIdWithRestaurant(userId);
    }

    @GetMapping("/filter")
    public List<Vote> getAllWithRestaurantByUserIdBetween(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        long userId = authUserId();
        log.info("getAllWithRestaurantByUserIdBetween {} - {} of user {}", startDate, endDate, userId);
        return voteService.getAllWithRestaurantByUserIdBetween(startDate, endDate, userId);
    }

    @PostMapping(value = "/by", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createUpdateWithLocation(@RequestParam long restaurantId) {
        long userId = authUserId();
        Vote vote = new Vote(LocalDate.now());
        log.info("save {} by user {} restaurant {}", vote, userId, restaurantId);

        Vote created = voteService.save(vote, authUserId(), restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_USER_VOTES + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        long userId = authUserId();
        log.info("delete by user {}", userId);
        voteService.delete(userId);
    }
}
