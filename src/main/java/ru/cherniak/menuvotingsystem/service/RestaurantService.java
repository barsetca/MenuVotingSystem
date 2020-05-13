package ru.cherniak.menuvotingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.repository.RestaurantRepository;

import java.util.List;

@Service
public class RestaurantService {

    private final Logger log = LoggerFactory.getLogger(RestaurantService.class);


    private final  RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant get(long id){
        log.info("get {}" , id);
        // return checkNotFoundWithId(restaurantRepository.get(id), id);
       return restaurantRepository.get(id);
    }

    public Restaurant create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        Assert.notNull(restaurant, "user must not be null");
        return restaurantRepository.save(restaurant);

    }

    public Restaurant update(Restaurant restaurant) {
        log.info("update {}", restaurant);
        Assert.notNull(restaurant, "user must not be null");
        // checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
        return restaurantRepository.save(restaurant);

    }

    public void delete(long id){
        log.info("delete {}", id);
        //checkNotFoundWithId(repository.delete(id), id);
        restaurantRepository.delete(id);
    }

    public Restaurant getByName(String name){
        log.info("getByName {}", name);
        Assert.notNull(name, "name must not be null");
        // return checkNotFound(restaurantRepository.getByName(name), "name=" + name);
        return restaurantRepository.getByName(name);
    }

   public List<Restaurant> getAll(){
        log.info("getAll");
        return restaurantRepository.getAll();
    }
}
