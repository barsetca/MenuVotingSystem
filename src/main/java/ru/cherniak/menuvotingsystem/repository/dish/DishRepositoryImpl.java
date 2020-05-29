package ru.cherniak.menuvotingsystem.repository.dish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.repository.restaurant.JpaRestaurantRepository;
import ru.cherniak.menuvotingsystem.repository.restaurant.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DishRepositoryImpl implements DishRepository {

    private static final Sort SORT_DATE_NAME = Sort.by(Sort.Order.desc("date"), Sort.Order.asc("name"));
    private static final Sort SORT_DATE_RID_NAME = Sort.by(Sort.Order.desc("date"), Sort.Order.asc("restaurant.id"), Sort.Order.asc("name"));
    private static final Sort SORT_RID_NAME = Sort.by(Sort.Direction.ASC, "restaurant.id", "name");
    private static final Sort SORT_NAME = Sort.by(Sort.Order.asc("name"));

    @Autowired
    private JpaDishRepository repository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public Dish save(Dish dish, long restaurantId) {
        Restaurant restaurant = restaurantRepository.get(restaurantId);
        if (restaurant == null) {
            return null;
        }
        dish.setRestaurant(restaurant);
        return repository.save(dish);
    }

    @Override
    public Dish get(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
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
    @Transactional
    public Dish getWithRestaurant(long id, long restaurantId) {
        return repository.findOneWithRestaurant(id, restaurantId);
    }

    @Override
    @Transactional
    public List<Dish> getDayMenuByDateWithRestaurant(LocalDate date, long restaurantId) {
        return repository.findAllByDateAndRestaurantIdWithRestaurant(date, restaurantId, SORT_NAME);
    }

    @Override
    @Transactional
    public List<Dish> getAllDayMenuByDateWithRestaurant (LocalDate date){
        return repository.findAllByDateWithRestaurant(date, SORT_RID_NAME);
    }
}
