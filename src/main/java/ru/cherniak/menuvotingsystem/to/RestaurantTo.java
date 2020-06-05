package ru.cherniak.menuvotingsystem.to;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Objects;

public class RestaurantTo extends BaseTo {

    private final String type;
    private final String address;
    private final String phone;
    private final String url;
    private final long countOfVotes;

    @ConstructorProperties({"id", "name", "type", "address", "phone", "url", "countOfVotes"})
    public RestaurantTo(long id, String name, String type, String address, String phone, String url, long countOfVotes) {
        super(id, name);
        this.type = type;
        this.address = address;
        this.phone = phone;
        this.url = url;
        this.countOfVotes = countOfVotes;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getUrl() {
        return url;
    }

    public long getCountOfVotes() {
        return countOfVotes;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTo that = (RestaurantTo) o;
        return countOfVotes == that.countOfVotes &&
                type.equals(that.type) &&
                address.equals(that.address) &&
                phone.equals(that.phone) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, address, phone, url, countOfVotes);
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", url='" + url + '\'' +
                ", countOfVotes=" + countOfVotes +
                '}';
    }
}
