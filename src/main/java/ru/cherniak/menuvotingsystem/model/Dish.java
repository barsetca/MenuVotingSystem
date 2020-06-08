package ru.cherniak.menuvotingsystem.model;


import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.cherniak.menuvotingsystem.View;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(
                columnNames = {"date", "name", "restaurant_id"}, name = "dishes_unique_date_name_restaurant_id_idx")})
public class Dish extends AbstractBaseNameId {

    @Column(name = "date", nullable = false, columnDefinition = "timestamp")
    @FutureOrPresent
    @NotNull
    private LocalDate date = LocalDate.now();

    @Column(name = "price", nullable = false)
    @Min(0)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(String name, Integer price) {
        this(null, name, LocalDate.now(), price);
    }

    public Dish(String name, LocalDate date, Integer price) {
        this(null, name, date, price);
    }

    public Dish(Long id, String name, LocalDate date, Integer price) {
        super(id, name);
        this.date = date;
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "date=" + date +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
