package ru.cherniak.menuvotingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.repository.dish.JpaDishRepository;
import ru.cherniak.menuvotingsystem.util.DateTimeUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFoundWithId;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFoundWithMsg;

@Service
@Transactional(readOnly = true)
public class DishService {

    private static final Sort SORT_DATE_NAME = Sort.by(Sort.Order.desc("date"), Sort.Order.asc("name"));
    private static final Sort SORT_DATE_RID_NAME = Sort.by(Sort.Order.desc("date"), Sort.Order.asc("restaurant.id"), Sort.Order.asc("name"));
    private static final Sort SORT_NAME = Sort.by(Sort.Order.asc("name"));


    private final JpaDishRepository dishRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public DishService(JpaDishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }


    private Dish save(Dish dish, long restaurantId) {
        if (!dish.isNew() && get(dish.id(), restaurantId) == null) {
            return null;
        }
        dish.setRestaurant(em.getReference(Restaurant.class, restaurantId));
        return dishRepository.save(dish);
    }

    @Transactional
    public Dish create(Dish dish, long restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        return checkNotFoundWithMsg(save(dish, restaurantId), "restaurantId= " + restaurantId);
    }

    @Transactional
    public void update(Dish dish, long restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        checkNotFoundWithId(save(dish, restaurantId), dish.getId());
    }

    @Transactional
    public void delete(long id, long restaurantId) {
        checkNotFoundWithId(dishRepository.delete(id, restaurantId) != 0, id);
    }

    public Dish get(long id, long restaurantId) {
        return checkNotFoundWithId(dishRepository.findOneByRestaurant( id, restaurantId).orElse(null), id);
    }

    public List<Dish> getAllWithRestaurant() {
        return dishRepository.findAllWithRestaurant(SORT_DATE_RID_NAME);
    }

    public List<Dish> getAllByRestaurant(long restaurantId) {
        return dishRepository.findAllByRestaurantId(restaurantId, SORT_DATE_NAME);
    }

    public List<Dish> getDayMenu(@Nullable LocalDate date, long restaurantId) {
        return dishRepository.findAllByDateAndRestaurantId(date, restaurantId, SORT_NAME);
    }

    public List<Dish> getTodayMenu(long restaurantId) {
        return getDayMenu(LocalDate.now(), restaurantId);
    }

    public List<Dish> getAllByRestaurantBetweenDatesInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate, long restaurantId) {
        return dishRepository.findAllByRestaurantIdAndDateBetween(restaurantId, DateTimeUtil.getStartDate(startDate),
                DateTimeUtil.getEndDate(endDate), SORT_DATE_NAME);
    }

    public Dish getWithRestaurant(long id, long restaurantId) {
        return checkNotFoundWithId(dishRepository.findOneWithRestaurant(id, restaurantId).orElse(null), id);
    }
}
