package ru.cherniak.menuvotingsystem.repository;

import ru.cherniak.menuvotingsystem.model.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    // false if not found
    boolean delete(long id);

    // null if not found
    User get(long id);

    // null if not found
    User getByEmail(String email);

    List<User> getAll();
}

