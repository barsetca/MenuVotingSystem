package ru.cherniak.menuvotingsystem.to;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Objects;

public class RestaurantTo extends BaseTo implements Serializable {
    private static final long serialVersionUID = 1L;

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
