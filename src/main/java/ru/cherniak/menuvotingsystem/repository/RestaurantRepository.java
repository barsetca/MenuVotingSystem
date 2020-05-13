package ru.cherniak.menuvotingsystem.repository;

import ru.cherniak.menuvotingsystem.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    Restaurant get(long id);

    Restaurant save(Restaurant restaurant);

    boolean delete(long id);

    Restaurant getByName(String name);

    List<Restaurant> getAll();
}
