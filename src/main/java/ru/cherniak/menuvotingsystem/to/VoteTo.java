package ru.cherniak.menuvotingsystem.to;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.Objects;

public class VoteTo extends BaseTo{

    private final LocalDate date;

    @ConstructorProperties({"id", "name", "date"})
    public VoteTo(Long id, String name, LocalDate date) {
        super(id, name);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteTo voteTo = (VoteTo) o;
        return date.equals(voteTo.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public String toString() {
        return "VoteTo{" +
                "date=" + date +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
