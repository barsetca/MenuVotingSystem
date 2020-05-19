package ru.cherniak.menuvotingsystem.repository.dish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.repository.restaurant.JpaRestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DishRepositoryImpl implements DishRepository {

    private static final Sort SORT_DATE_PRICE = Sort.by(Sort.Direction.DESC, "date", "price");
    private static final Sort SORT_PRICE = Sort.by(Sort.Direction.DESC, "price");

    @Autowired
    private JpaDishRepository repository;

    @Autowired
    private JpaRestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public Dish save(Dish dish, long restaurantId) {
        if (!dish.isNew() && get(dish.getId(), restaurantId) == null) {
            return null;
        }
        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        dish.setRestaurant(restaurant);
        return repository.save(dish);
    }

    @Override
    public Dish get(long id, long restaurantId) {
        return repository.findByIdAndRestaurantId(id, restaurantId).orElse(null);
    }

    @Override
    @Transactional
    public boolean delete(long id, long restaurantId) {
        return repository.delete(id, restaurantId) != 0;
    }

    @Override
    public List<Dish> getAll(long restaurantId) {
        return repository.findAllByRestaurantId(restaurantId, SORT_DATE_PRICE);
    }

    @Override
    public List<Dish> getDayMenu(LocalDate date, long restaurantId) {
        return repository.findAllByDateAndRestaurantId(date, restaurantId, SORT_PRICE);
    }

    @Override
    public List<Dish> getAllByRestaurantBetweenInclusive(LocalDate startDate, LocalDate endDate, long restaurantId) {
        return repository.findAllByRestaurantIdAndDateBetween(restaurantId, startDate, endDate, SORT_DATE_PRICE);
    }
//    return crudRepository.findAll(userId, DateTimeUtil.getStartInclusive(startDate), DateTimeUtil.getEndExclusive(endDate));

    @Override
    @Transactional
    public Dish getWithRestaurant(long id, long restaurantId) {
        return repository.findOneWithRestaurant(id, restaurantId);
    }

    @Override
    @Transactional
    public List<Dish> getDayMenuByDateWithRestaurant(LocalDate date, long restaurantId) {
        return repository.getAllByDateAndRestaurantIdWithRestaurant(date, restaurantId, SORT_DATE_PRICE);
    }

    @Override
    @Transactional
    public List<Dish> getAllDayMenuByDateWithRestaurant (LocalDate date){
        return repository.getAllByDateWithRestaurant(date);
    }
}
