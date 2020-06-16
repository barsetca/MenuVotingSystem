package ru.cherniak.menuvotingsystem.repository.dish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
public class DishRepositoryImpl implements DishRepository {

    private static final Sort SORT_DATE_NAME = Sort.by(Sort.Order.desc("date"), Sort.Order.asc("name"));
    private static final Sort SORT_DATE_RID_NAME = Sort.by(Sort.Order.desc("date"), Sort.Order.asc("restaurant.id"), Sort.Order.asc("name"));
    private static final Sort SORT_NAME = Sort.by(Sort.Order.asc("name"));

    @Autowired
    private JpaDishRepository repository;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Dish save(Dish dish, long restaurantId) {
        if (!dish.isNew() && get(dish.id()) == null) {
            return null;
        }
        dish.setRestaurant(em.getReference(Restaurant.class, restaurantId));
        return repository.save(dish);
    }

    @Override
    public Dish get(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
//    @Transactional
    public List<Dish> getAllWithRestaurant() {
        return repository.findAllWithRestaurant(SORT_DATE_RID_NAME);
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        return repository.delete(id) != 0;
    }

    @Override
    public List<Dish> getAllByRestaurant(long restaurantId) {
        return repository.findAllByRestaurantId(restaurantId, SORT_DATE_NAME);
    }

    @Override
    public List<Dish> getDayMenu(LocalDate date, long restaurantId) {
        return repository.findAllByDateAndRestaurantId(date, restaurantId, SORT_NAME);
    }

    @Override
    public List<Dish> getAllByRestaurantBetweenInclusive(LocalDate startDate, LocalDate endDate, long restaurantId) {
        return repository.findAllByRestaurantIdAndDateBetween(restaurantId, startDate, endDate, SORT_DATE_NAME);
    }

    @Override
//    @Transactional
    public Dish getWithRestaurant(long id, long restaurantId) {
        return repository.findOneWithRestaurant(id, restaurantId).orElse(null);
    }
}
