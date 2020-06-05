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
import ru.cherniak.menuvotingsystem.to.VoteTo;
import ru.cherniak.menuvotingsystem.util.VoteUtil;

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
    public VoteTo get(@PathVariable long id) {
        long userId = authUserId();
        log.info("get {} by user {}", id, userId);
        return voteService.getVoteTo(id, userId);
    }

    @GetMapping()
    public List<VoteTo> getAll() {
        long userId = authUserId();
        log.info("getAll by user {}", userId);
        return voteService.getAllVoteTos(userId);
    }

    @GetMapping("/filter")
    public List<VoteTo> getAllBetweenInclusive(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        long userId = authUserId();
        log.info("getAllBetweenInclusive {} - {} of user {}", startDate, endDate, userId);
        return voteService.getVoteTosBetweenInclusive(startDate, endDate, userId);
    }

    @PostMapping(value = "/by", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> createUpdateWithLocation(@RequestParam long restaurantId) {
        long userId = authUserId();
        log.info("createUpdateWithLocation by user {} restaurant {}", userId, restaurantId);
        Vote created = voteService.save(authUserId(), restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_USER_VOTES + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(VoteUtil.createTo(created));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        long userId = authUserId();
        log.info("delete by user {}", userId);
        voteService.delete(userId);
    }
}
