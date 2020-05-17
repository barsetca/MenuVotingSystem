package ru.cherniak.menuvotingsystem.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NamedQueries({
        @NamedQuery(name = Vote.DELETE, query = "DELETE FROM Vote v WHERE v.date=:date AND v.user.id=:userId"),
        @NamedQuery(name = Vote.GET, query = "SELECT v FROM Vote v WHERE v.date=:date AND v.user.id=:userId"),
        @NamedQuery(name = Vote.GET_ALL_SORTED, query = "SELECT v FROM Vote v ORDER BY v.date DESC , v.restaurant.id ASC"),
        @NamedQuery(name = Vote.GET_NUMBER_BY_DATE, query = "SELECT COUNT (v) FROM Vote v WHERE v.date=:date AND v.restaurant.id=:restaurantId"),
        @NamedQuery(name = Vote.GET_TOTAL_NUMBER, query = "SELECT COUNT (v) FROM Vote  v WHERE v.restaurant.id=:restaurantId"),
        @NamedQuery(name = Vote.GET_NUMBER_BETWEEN,
                query = "SELECT COUNT (v) FROM Vote v WHERE v.date>= :startDate AND v.date<= :endDate AND v.restaurant.id=:restaurantId")
})

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"date", "user_id"}, name = "votes_unique_date_user_id_idx")})
public class Vote extends AbstractBase {

    public static final String DELETE = "Vote.delete";
    public static final String GET = "Vote.get";
    public static final String GET_ALL_SORTED = "Vote.getAll";
    public static final String GET_NUMBER_BY_DATE = "Vote.numberByDate";
    public static final String GET_TOTAL_NUMBER = "Vote.totalNumber";
    public static final String GET_NUMBER_BETWEEN = "Vote.numberBetWeen";


    @Column(name = "date", nullable = false, columnDefinition = "timestamp")
    @NotNull
    private LocalDate date;


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
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
                ", id=" + id +
                '}';
    }
}
