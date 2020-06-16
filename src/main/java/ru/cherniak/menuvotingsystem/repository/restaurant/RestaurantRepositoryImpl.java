package ru.cherniak.menuvotingsystem.repository.restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private static final Sort SORT_BY_NAME = Sort.by(Sort.Direction.ASC, "name");

    @Autowired
    private JpaRestaurantRepository repository;

    @Override
    public Restaurant get(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Restaurant save(Restaurant restaurant) {
        if (!restaurant.isNew() && get(restaurant.id()) == null) {
            return null;
        }
        return repository.save(restaurant);
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        return repository.deleteById(id) != 0;
    }

    @Override
    public Restaurant getByName(String name) {
        return repository.getByName(name).orElse(null);
    }

    @Override
    public List<Restaurant> getAll() {
        return repository.findAll(SORT_BY_NAME);
    }

    @Override
    @Transactional
    public Restaurant getWithVotes(long id) {
        return repository.findOneWithVotes(id);
    }

    @Override
    @Transactional
    public Restaurant getWithDishes(long id) {
        return repository.findOneWithDishes(id);
    }

    @Override
    @Transactional
    public List<Restaurant> getAllWithDishes() {
        return repository.findAllWithDishes(SORT_BY_NAME);
    }


    @Override
//    @Transactional
    public List<Restaurant> getAllWithVotes() {
        return repository.findAllWithVotes();
    }

    @Override
    public Restaurant getByNameWithVotes(String name) {
        return repository.findOneByNameWithVotes(name).orElse(null);
    }
}
