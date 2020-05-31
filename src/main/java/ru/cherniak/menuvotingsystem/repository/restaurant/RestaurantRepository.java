package ru.cherniak.menuvotingsystem.repository.restaurant;

import ru.cherniak.menuvotingsystem.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    Restaurant get(long id);

    Restaurant save(Restaurant restaurant);

    boolean delete(long id);

    Restaurant getByName(String name);

    Restaurant getByNameWithVotes(String name);

    List<Restaurant> getAll();

    Restaurant getWithVotes(long id);

    Restaurant getWithDishes(long id);

    List<Restaurant> getAllWithDishes();

    List<Restaurant> getAllWithVotes();

}
