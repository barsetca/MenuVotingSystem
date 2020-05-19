package ru.cherniak.menuvotingsystem.repository.restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Restaurant;

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
        return repository.save(restaurant);
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        return repository.deleteById(id) != 0;
    }

    @Override
    public Restaurant getByName(String name) {
        return repository.getByName(name);
    }

    @Override
    public List<Restaurant> getAll() {
        return repository.findAll(SORT_BY_NAME);
    }

    @Override
    @Transactional
    public Restaurant getWithListVotes(long id) {
        return repository.findOneWithVotes(id);
    }

    @Override
    @Transactional
    public Restaurant getWithListDishes(long id) {
        return repository.findOneWithDishes(id);
    }

    @Override
    @Transactional
    public List<Restaurant> findAllWithDishes(){
        return repository.findAllWithDishes(SORT_BY_NAME);
    }
    @Override
    @Transactional
    public List<Restaurant> findAllWithVotes(){
        return repository.findAllWithVotes(SORT_BY_NAME);
    }

//    @Override
//    @Transactional
//    public Restaurant getWithDishesAndVotes(long id){
//        return repository.findOneByIdWithDishesAndVotes(id, SORT_BY_NAME);
//    }

}
