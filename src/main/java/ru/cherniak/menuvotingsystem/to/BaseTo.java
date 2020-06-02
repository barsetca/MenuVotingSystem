package ru.cherniak.menuvotingsystem.to;

import ru.cherniak.menuvotingsystem.HasId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public abstract class BaseTo implements HasId {

    protected Long id;

    @NotBlank
    @Size(min = 2, max = 100, message = "length must be between 2 and 100 characters")
    protected String name;

    public BaseTo() {
    }

    public BaseTo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

