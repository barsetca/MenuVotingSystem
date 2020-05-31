package ru.cherniak.menuvotingsystem.util;

import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.to.RestaurantTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantUtil {

    private RestaurantUtil() {
    }

    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getType(), restaurant.getAddress(), restaurant.getPhone(),
                restaurant.getUrl(), restaurant.getVotes().size());
    }

    public static List<RestaurantTo> getRestaurantTosSortedByCountVotes(Collection<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::createTo).collect(Collectors.toList());
    }
}
