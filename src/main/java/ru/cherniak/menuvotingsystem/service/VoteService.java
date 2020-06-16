package ru.cherniak.menuvotingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.repository.vote.VoteRepository;
import ru.cherniak.menuvotingsystem.to.VoteTo;
import ru.cherniak.menuvotingsystem.util.DateTimeUtil;
import ru.cherniak.menuvotingsystem.util.VoteUtil;

import java.time.LocalDate;
import java.util.List;

import static ru.cherniak.menuvotingsystem.util.DateTimeUtil.checkTimeBorder;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.*;

@Service
public class VoteService {

    private final VoteRepository repository;

    @Autowired
    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Vote save(long userId, long restaurantId) {
        if (repository.getByDateNow(userId) != null) {
            checkTimeBorder();
            repository.delete(LocalDate.now(), userId);
        }
        Vote vote = new Vote(LocalDate.now());
        return checkNotFoundWithMsg(repository.save(vote, userId, restaurantId), "restaurantId" + restaurantId);
    }

    public Vote getWithRestaurantToday(long userId){
        return checkNotFoundWithMsg(repository.getWithRestaurantByDateNow(userId), "userId" + userId);
    }

    public VoteTo getVoteToToday(long userId){
        Vote voteWithRestaurantToday = getWithRestaurantToday(userId);
        return VoteUtil.createTo(voteWithRestaurantToday);
    }


    public VoteTo getVoteTo(long id, long userId) {
        Vote voteWithRestaurant = checkNotFoundWithId(repository.getWithRestaurant(id, userId), id);
        return VoteUtil.createTo(voteWithRestaurant);
    }

    public void delete(long userId) {
        checkTimeBorder();
        LocalDate date = LocalDate.now();
        checkNotFound(repository.delete(date, userId), "date: " + date);
    }

    public List<VoteTo> getAllVoteTos(long userId) {
        List<Vote> votesWithRestaurant = repository.getAllByUserIdWithRestaurant(userId);
        return VoteUtil.getVoteTos(votesWithRestaurant);
    }

    public List<VoteTo> getVoteTosBetweenInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate, long userId) {
        List<Vote> votesWithRestaurantBetween = repository.getAllWithRestaurantByUserIdBetween(
                DateTimeUtil.getStartDate(startDate),
                DateTimeUtil.getEndDate(endDate), userId);
        return VoteUtil.getVoteTos(votesWithRestaurantBetween);
    }
}
