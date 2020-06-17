package ru.cherniak.menuvotingsystem.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.cherniak.menuvotingsystem.util.DateTimeUtil;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurants_unique_name_idx")})
public class Restaurant extends AbstractBaseNameId {

     @Column(name = "address", nullable = false)
    @NotBlank
    @NotNull
    @Size(min = 5, max = 100)
    private String address;

    @Column(name = "phone", nullable = false)
    @NotBlank
    @NotNull
    @Size(min = 4, max = 100)
    private String phone;

    @Column(name = "url", nullable = false)
    @NotNull
    private String url;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @OrderBy("date DESC, name ASC")
    private Set<Dish> dishes;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @OrderBy("date DESC, restaurant ASC")
    private Set<Vote> votes;

    public Restaurant() {
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getPhone(), restaurant.getUrl());
    }

    public Restaurant(String name, String address, String phone, String url) {
        this(null, name, address, phone, url);
    }

    public Restaurant(String name, String address, String phone) {
        this(null, name, address, phone, "");
    }

    public Restaurant(Long id, String name, String address, String phone) {
        this(id, name, address, phone, "");
    }

    public Restaurant(Long id, String name, String address, String phone, String url) {
        super(id, name);
        this.address = address;
        this.url = url;
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(Set<Dish> dishes) {
        this.dishes = dishes;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                ", address= " + address +
                ", phone= " + phone +
                ", url= " + url +
                ", name= " + name +
                ", id= " + id +
                '}';
    }
}
