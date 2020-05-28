package ru.cherniak.menuvotingsystem.model;


import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.hibernate.annotations.Cache;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurants_unique_name_idx")})
public class Restaurant extends AbstractBaseNameId {

    @Column(name = "type", nullable = false)
    @NotBlank
    @NotNull
    @Size(min = 3, max = 100)
    private String type;


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

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private Date registered = new Date();

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
        this(restaurant.getId(), restaurant.getName(), restaurant.getType(), restaurant.getAddress(), restaurant.getPhone(), restaurant.getUrl(),
                restaurant.getRegistered());
    }

    public Restaurant(String name, String type, String address, String phone, String url) {
        this(null, name, type, address, phone, url, new Date());
    }

    public Restaurant(String name, String type, String address, String phone) {

        this(null, name, type, address, phone, "", new Date());
    }

    public Restaurant(Long id, String name, String type, String address, String phone) {
        this(id, name, type, address, phone, "", new Date());
    }

    public Restaurant(Long id, String name, String type, String address, String phone, String url) {
        this(id, name, type, address, phone, url, new Date());
    }

    public Restaurant(Long id, String name, String type, String address, String phone, String url, Date registered) {
        super(id, name);
        this.type = type;
        this.address = address;
        this.url = url;
        this.phone = phone;
        this.registered = registered;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", url='" + url + '\'' +
                ", registered=" + registered +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
