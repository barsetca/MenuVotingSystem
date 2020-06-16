package ru.cherniak.menuvotingsystem.web.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.cherniak.menuvotingsystem.AuthorizedUser;
import ru.cherniak.menuvotingsystem.View;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.service.VoteService;
import ru.cherniak.menuvotingsystem.to.VoteTo;
import ru.cherniak.menuvotingsystem.util.VoteUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = UserVoteRestController.REST_USER_VOTES, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserVoteRestController {

    protected static final String REST_USER_VOTES = "/rest/user/votes";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private VoteService voteService;

    @GetMapping("/{id}")
    public VoteTo get(@PathVariable long id, @AuthenticationPrincipal AuthorizedUser authUser) {
        long userId = authUser.getId();
        log.info("get {} by user {}", id, userId);
        return voteService.getVoteTo(id, userId);
    }

    @GetMapping("/today")
    public VoteTo getToday(@AuthenticationPrincipal AuthorizedUser authUser) {
        long userId = authUser.getId();
        log.info("getToday by user {}", userId);
        return voteService.getVoteToToday(userId);
    }

    @GetMapping()
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthorizedUser authUser) {
        long userId = authUser.getId();
        log.info("getAll by user {}", userId);
        return voteService.getAllVoteTos(userId);
    }

    @GetMapping("/filter")
    public List<VoteTo> getAllBetweenInclusive(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate,
                                               @AuthenticationPrincipal AuthorizedUser authUser) {
        long userId = authUser.getId();
        log.info("getAllBetweenInclusive {} - {} of user {}", startDate, endDate, userId);
        return voteService.getVoteTosBetweenInclusive(startDate, endDate, userId);
    }

    @PostMapping(value = "/by", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> createWithLocation(@RequestParam long restaurantId,
                                                     @AuthenticationPrincipal AuthorizedUser authUser) {
        long userId = authUser.getId();
        log.info("createWithLocation by user {} restaurant {}", userId, restaurantId);
        Vote created = voteService.save(userId, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_USER_VOTES + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(VoteUtil.createTo(created));
    }


    @PutMapping(value = "/by", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestParam long restaurantId,
                       @AuthenticationPrincipal AuthorizedUser authUser) {
        long userId = authUser.getId();
        log.info("update by user {} restaurant {}", userId, restaurantId);
        voteService.save(userId, restaurantId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authUser) {
        long userId = authUser.getId();
        log.info("delete by user {}", userId);
        voteService.delete(userId);
    }
}
