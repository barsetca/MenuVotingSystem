package ru.cherniak.menuvotingsystem.repository.user;

import ru.cherniak.menuvotingsystem.model.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    boolean delete(long id);

    User get(long id);

    User getByEmail(String email);

    List<User> getAll();

    User getWithVotes(long id);
}

