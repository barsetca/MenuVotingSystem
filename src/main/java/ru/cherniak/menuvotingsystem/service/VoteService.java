package ru.cherniak.menuvotingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.repository.JpaRestaurantRepository;
import ru.cherniak.menuvotingsystem.repository.JpaUserRepository;
import ru.cherniak.menuvotingsystem.repository.JpaVoteRepository;
import ru.cherniak.menuvotingsystem.to.VoteTo;
import ru.cherniak.menuvotingsystem.util.DateTimeUtil;
import ru.cherniak.menuvotingsystem.util.VoteUtil;

import java.time.LocalDate;
import java.util.List;

import static ru.cherniak.menuvotingsystem.util.DateTimeUtil.checkTimeBorder;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.*;

@Service
@Transactional(readOnly = true)
public class VoteService {

    private static final Sort SORT_DATE = Sort.by(Sort.Order.desc("date"));

    private final JpaVoteRepository voteRepository;
    private final JpaUserRepository userRepository;
    private final JpaRestaurantRepository restaurantRepository;

    @Autowired
    public VoteService(JpaVoteRepository voteRepository, JpaUserRepository userRepository, JpaRestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Vote create(long userId, long restaurantId) {

        if (getByDateNow(userId) != null) {
            throw new IllegalArgumentException("Today's vote by " + userId + " already exists");
        }
        Vote vote = new Vote(LocalDate.now());
        vote.setUser(userRepository.getOne(userId));
        vote.setRestaurant(restaurantRepository.findById(restaurantId).orElse(null));

        return voteRepository.save(vote);
    }

    @Transactional
    public Vote update(long userId, long restaurantId) {

        Vote update = getByDateNow(userId);
        if (update == null) {
            return null;
        }
        checkTimeBorder();
        update.setRestaurant(restaurantRepository.findById(restaurantId).orElse(null));
        return voteRepository.save(update);
    }

    @Transactional
    public void delete(long userId) {
        checkTimeBorder();
        LocalDate date = LocalDate.now();
        checkNotFound(voteRepository.delete(date, userId) != 0, "date: " + date);
    }

    public Vote getByDateNow(long userId) {
        return voteRepository.findOneByDateAndUserId(LocalDate.now(), userId).orElse(null);
    }

    public Vote getWithRestaurantToday(long userId) {
        return checkNotFoundWithMsg(
                voteRepository.findOneWithRestaurantByDateAndUserId(LocalDate.now(), userId).orElse(null),
                "userId" + userId);
    }

    @Transactional
    public VoteTo getVoteToToday(long userId) {
        Vote voteWithRestaurantToday = getWithRestaurantToday(userId);
        return VoteUtil.createTo(voteWithRestaurantToday);
    }


    public Vote getWithRestaurant(long id, long userId) {
        return checkNotFoundWithId(voteRepository.findByDateAndUserIdWithRestaurant(id, userId).orElse(null), id);
    }

    public VoteTo getVoteTo(long id, long userId) {
        Vote voteWithRestaurant = checkNotFoundWithId(getWithRestaurant(id, userId), id);
        return VoteUtil.createTo(voteWithRestaurant);
    }

    public List<Vote> getAllByUserIdWithRestaurant(long userId) {
        return voteRepository.findAllByUserIdWithRestaurant(userId, SORT_DATE);
    }

    public List<VoteTo> getAllVoteTos(long userId) {
        List<Vote> votesWithRestaurant = getAllByUserIdWithRestaurant(userId);
        return VoteUtil.getVoteTos(votesWithRestaurant);
    }

    public List<Vote> getAllWithRestaurantByUserIdBetween(LocalDate startDate, LocalDate endDate, long userId) {
        return voteRepository.findAllWithRestaurantByUserIdAndDateBetween(startDate, endDate, userId, SORT_DATE);
    }

    public List<VoteTo> getVoteTosBetweenInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate, long userId) {
        List<Vote> votesWithRestaurantBetween = getAllWithRestaurantByUserIdBetween(
                DateTimeUtil.getStartDate(startDate),
                DateTimeUtil.getEndDate(endDate), userId);
        return VoteUtil.getVoteTos(votesWithRestaurantBetween);
    }
}
