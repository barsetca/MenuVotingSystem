package ru.cherniak.menuvotingsystem.repository.restaurant;

import ru.cherniak.menuvotingsystem.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    Restaurant get(long id);

    Restaurant save(Restaurant restaurant);

    boolean delete(long id);

    Restaurant getByName(String name);

    List<Restaurant> getAll();

    Restaurant getWithListVotes(long id);

    Restaurant getWithListDishes(long id);

    List<Restaurant> findAllWithDishes();

    List<Restaurant> findAllWithVotes();

    List<Restaurant> getAllWithDishesAndVotes();
}
