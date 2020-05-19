package ru.cherniak.menuvotingsystem.model;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = Restaurant.DELETE, query = "DELETE FROM Restaurant r WHERE r.id =:id"),
        @NamedQuery(name = Restaurant.GET_ALL, query = "SELECT r  FROM Restaurant r ORDER BY r.name"),
        @NamedQuery(name = Restaurant.GET_BY_NAME, query = "SELECT r FROM Restaurant  r WHERE r.name=:name")
})

@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurants_unique_name_idx")})
public class Restaurant extends AbstractBaseNameId {

    public static final String DELETE = "Restaurant.delete";
    public static final String GET_ALL = "Restaurant.getAll";
    public static final String GET_BY_NAME = "Restaurant.getByName";

    @Column(name = "address", nullable = false)
    @NotNull
    @Size(min = 5)
    private String address;

    @Column(name = "phone", nullable = false)
    @NotNull
    @Size(min = 4)
    private String phone;

    @Column(name = "url", nullable = false)
    @NotNull
    private String url;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private Date registered = new Date();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @OrderBy("date DESC, price DESC")
    private Set<Dish> dishes;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @OrderBy("date DESC, restaurant ASC")
    private Set<Vote> votes;

    public Restaurant() {
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getPhone(), restaurant.getUrl(),
                restaurant.getRegistered());
    }

    public Restaurant(String name, String address, String phone, String url) {
        this(null, name, address, phone, url, new Date());
    }

    public Restaurant(String name, String address, String phone) {

        this(null, name, address, phone, "", new Date());
    }

    public Restaurant(Long id, String name, String address, String phone) {
        this(id, name, address, phone, "", new Date());
    }

    public Restaurant(Long id, String name, String address, String phone, String url) {
        this(id, name, address, phone, url, new Date());
    }

    public Restaurant(Long id, String name, String address, String phone, String url, Date registered) {
        super(id, name);
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

    @Override
    public String toString() {
        return "Restaurant{" +
                "address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", url='" + url + '\'' +
                ", registered=" + registered +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
