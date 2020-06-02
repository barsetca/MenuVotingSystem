package ru.cherniak.menuvotingsystem;

import org.springframework.util.Assert;

public interface HasId {

    Long getId();

    void setId(Long id);

    default boolean isNew() {
        return getId() == null;
    }

    default long id() {
        Assert.notNull(getId(), "Entity must has id");
        return getId();
    }
}
