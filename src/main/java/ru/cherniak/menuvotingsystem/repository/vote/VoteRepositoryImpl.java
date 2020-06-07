package ru.cherniak.menuvotingsystem.repository.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.repository.restaurant.JpaRestaurantRepository;
import ru.cherniak.menuvotingsystem.repository.restaurant.RestaurantRepository;
import ru.cherniak.menuvotingsystem.repository.user.JpaUserRepository;
import ru.cherniak.menuvotingsystem.repository.user.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.time.LocalDate;
import java.util.List;


@Repository
@Transactional(readOnly = true)
public class VoteRepositoryImpl implements VoteRepository {

    private static final Sort SORT_DATE = Sort.by(Sort.Order.desc("date"));

    @Autowired
    private JpaVoteRepository repository;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Override
    @Transactional
    public Vote save(Vote vote, long userId, long restaurantId) {

        repository.delete(vote.getDate(), userId);
        vote.setUser(em.getReference(User.class, userId));
        vote.setRestaurant(em.getReference(Restaurant.class, restaurantId));
        return repository.save(vote);
    }

    @Override
    @Transactional
    public Vote getWithRestaurant(long id, long userId) {
        return repository.findByDateAndUserIdWithRestaurant(id, userId).orElse(null);
    }

    @Override
    @Transactional
    public boolean delete(LocalDate date, long userId) {
        return repository.delete(date, userId) != 0;
    }

    @Override
    public long countByRestaurant(long restaurantId) {
        return repository.countAllByRestaurantId(restaurantId);
    }

    @Override
    @Transactional
    public List<Vote> getAllByUserIdWithRestaurant(long userId) {
        return repository.findAllByUserIdWithRestaurant(userId, SORT_DATE);
    }

    @Override
    @Transactional
    public List<Vote> getAllWithRestaurantByUserIdBetween(LocalDate startDate, LocalDate endDate, long userId) {
        return repository.findAllWithRestaurantByUserIdAndDateBetween(startDate, endDate, userId, SORT_DATE);
    }
}
