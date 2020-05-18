package ru.cherniak.menuvotingsystem.repository.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.repository.restaurant.JpaRestaurantRepository;
import ru.cherniak.menuvotingsystem.repository.user.JpaUserRepository;

import java.time.LocalDate;
import java.util.List;


@Repository
@Transactional(readOnly = true)
public class VoteRepositoryImpl implements VoteRepository {

    private static final Sort SORT_DATE_RESTAURANT = Sort.by(Sort.Order.desc("date"), Sort.Order.asc("restaurantId"));

    @Autowired
    private JpaVoteRepository repository;

    @Autowired
    private JpaRestaurantRepository restaurantRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Override
    @Transactional
    public Vote save(Vote vote, long userId, long restaurantId) {
        repository.delete(vote.getDate(), userId);
        vote.setRestaurant(restaurantRepository.getOne(restaurantId));
        vote.setUser(userRepository.getOne(userId));
        return repository.save(vote);
    }

    @Override
    public Vote get(LocalDate date, long userId) {
        return repository.findByDateAndUserId(date, userId).orElse(null);
    }

    @Override
    @Transactional
    public boolean delete(LocalDate date, long userId) {
        return repository.delete(date, userId) != 0;
    }

    @Override
    public long countByDateAndRestaurant(LocalDate date, long restaurantId) {
        return repository.countAllByDateAndRestaurantId(date, restaurantId);
    }

    @Override
    public long countByRestaurant(long restaurantId) {
        return repository.countAllByRestaurantId(restaurantId);
    }

    @Override
    public long countByRestaurantAndDateBetween(LocalDate startDate, LocalDate endDate, long restaurantId) {
        return repository.countAllByRestaurantIdAndDateBetween(restaurantId, startDate, endDate);
    }

    @Override
    public List<Vote> getAll() {
        return repository.findAll(SORT_DATE_RESTAURANT);
    }

    @Override
    @Transactional
    public Vote getOneByDateWithUserAndRestaurant(LocalDate date, long userId) {
        return repository.getOneByDateWithUserAndRestaurant(date, userId);
    }

    @Override
    @Transactional
    public List<Vote> getAllByDateWithRestaurantAndUser(LocalDate date) {
        return repository.getAllByDateWithRestaurantAndUser(date);
    }
}
