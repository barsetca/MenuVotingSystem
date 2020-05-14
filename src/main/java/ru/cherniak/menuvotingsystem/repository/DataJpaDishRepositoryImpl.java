package ru.cherniak.menuvotingsystem.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataJpaDishRepositoryImpl implements DishRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Dish save(Dish dish, long restaurantId) {
        //  Restaurant restaurant = em.r
        if (!dish.isNew() && get(dish.getId(), restaurantId) == null) {
            return null;
        }
        dish.setRestaurant(em.getReference(Restaurant.class, restaurantId));
        if (dish.isNew()) {
            em.persist(dish);
            return dish;
        } else {
            return em.merge(dish);
        }
    }

    @Override
    public Dish get(long id, long restaurantId) {
        Dish dish = em.find(Dish.class, id);
        return dish != null && dish.getRestaurant().getId() == restaurantId ? dish : null;
    }

    @Override
    @Transactional
    public boolean delete(long id, long restaurantId) {
        return em.createNamedQuery(Dish.DELETE)
                .setParameter("id", id)
                .setParameter("restaurantId", restaurantId)
                .executeUpdate() != 0;
    }

    @Override
    public List<Dish> getAll(long restaurantId) {
        return em.createNamedQuery(Dish.GET_ALL, Dish.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }

    @Override
    public List<Dish> getDayMenu(LocalDate date, long restaurantId) {
        return em.createNamedQuery(Dish.GET_DAY_MENU, Dish.class)
                .setParameter("date", date)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }

    @Override
    public List<Dish> getAllBetweenInclusive(LocalDate startDate, LocalDate endDate, long restaurantId) {
        return em.createNamedQuery(Dish.GET_MENU_BETWEEN, Dish.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }

//    @Override
//    public List<Dish> getDayMenuWithRestaurant(LocalDate date, int restaurantId) {
//        return null;
//    }
}
