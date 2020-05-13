package ru.cherniak.menuvotingsystem.model;


import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
@Table(name = "dishes",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"date", "name", "restaurant_id"}, name = "dishes_unique_date_name_restaurant_id_idx")})
public class Dish extends AbstractBaseNameId {

    @Column(name = "date", nullable = false, columnDefinition = "timestamp")
    @FutureOrPresent
    @NotNull
    private LocalDate date;

    @Column(name = "price", nullable = false)
    @Min(0)
    private Integer price;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Restaurant.class)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;


    public Dish() {
    }

    public Dish(String name, Integer price) {
        this(null, name, LocalDate.now(), price);
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
