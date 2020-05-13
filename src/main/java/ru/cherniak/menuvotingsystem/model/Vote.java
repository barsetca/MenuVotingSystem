package ru.cherniak.menuvotingsystem.model;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "votes" , uniqueConstraints = {@UniqueConstraint(
        columnNames = {"date", "user_id"}, name = "votes_unique_date_user_id_idx")})
public class Vote extends AbstractBase{

    @Column(name = "date", nullable = false, columnDefinition = "timestamp")
    @NotNull
    private LocalDate date;


    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    public Vote() {
    }

    public Vote(LocalDate date) {
      this(null, LocalDate.now());
    }

    public Vote(Long id, LocalDate date) {
        super(id);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "date=" + date +
                ", restaurant=" + restaurant.getName() +
                ", user=" + user.getName() +
                ", id=" + id +
                '}';
    }
}
