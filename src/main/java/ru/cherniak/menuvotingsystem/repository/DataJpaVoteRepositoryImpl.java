package ru.cherniak.menuvotingsystem.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.model.Vote;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataJpaVoteRepositoryImpl implements VoteRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Vote save(Vote vote, long userId, long restaurantId) {
        vote.setRestaurant(em.getReference(Restaurant.class, restaurantId));
        vote.setUser(em.getReference(User.class, userId));
        Vote checkExist = get(vote.getDate(), userId);
        if (checkExist == null) {
            em.persist(vote);
            return vote;
        } else {
            vote.setId(checkExist.getId());
            return em.merge(vote);
        }
    }

    @Override
    public Vote get(LocalDate date, long userId) {

        return em.createNamedQuery(Vote.GET, Vote.class)
                .setParameter("date", date)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst().orElse(null);
    }

    @Override
    @Transactional
    public boolean delete(LocalDate date, long userId) {
        return em.createNamedQuery(Vote.DELETE)
                .setParameter("date", date)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public long getRestaurantVotesByDate(LocalDate date, long restaurantId) {
        return em.createNamedQuery(Vote.GET_NUMBER_BY_DATE, Long.class)
                .setParameter("date", date)
                .setParameter("restaurantId", restaurantId)
                .getSingleResult();
    }

    @Override
    public long getAllRestaurantVotes(long restaurantId) {
        return em.createNamedQuery(Vote.GET_TOTAL_NUMBER, Long.class)
                .setParameter("restaurantId", restaurantId)
                .getSingleResult();
    }

    @Override
    public long getAllRestaurantVotesBetween(LocalDate startDate, LocalDate endDate, long restaurantId) {
        return em.createNamedQuery(Vote.GET_NUMBER_BETWEEN, Long.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("restaurantId", restaurantId)
                .getSingleResult();
    }

    @Override
    public List<Vote> getAll() {
        return em.createNamedQuery(Vote.GET_ALL_SORTED, Vote.class)
                .getResultList();
    }
}
