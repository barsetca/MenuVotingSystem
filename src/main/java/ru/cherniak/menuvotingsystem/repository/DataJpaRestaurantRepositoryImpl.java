package ru.cherniak.menuvotingsystem.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Restaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataJpaRestaurantRepositoryImpl implements RestaurantRepository{

    @PersistenceContext
    private EntityManager em;


    @Override
    public Restaurant get(long id) {
        return em.find(Restaurant.class, id);
    }

    @Override
    @Transactional
    public Restaurant save(Restaurant restaurant) {
        if (restaurant.isNew()){
            em.persist(restaurant);
            return restaurant;
        } else {
             return em.merge(restaurant);
        }
    }

    @Override
    @Transactional
    public boolean delete(long id) {

        return em.createNamedQuery(Restaurant.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public Restaurant getByName(String name) {
        return em.createNamedQuery(Restaurant.GET_BY_NAME, Restaurant.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Override
    public List<Restaurant> getAll() {
        return em.createNamedQuery(Restaurant.GET_ALL, Restaurant.class)
                .getResultList();
    }
}
