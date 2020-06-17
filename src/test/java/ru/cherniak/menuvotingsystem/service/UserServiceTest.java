package ru.cherniak.menuvotingsystem.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.cherniak.menuvotingsystem.VoteTestData;
import ru.cherniak.menuvotingsystem.model.Role;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.cherniak.menuvotingsystem.UserTestData.*;
import static ru.cherniak.menuvotingsystem.VoteTestData.VOTE_MATCHER;

class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private UserService service;

    @Test
    void create() {
        User newUser = new User(null, "CreateUser", "create@gmail.com", "newPass", true,
                LocalDateTime.now(), Set.of(Role.ROLE_USER, Role.ROLE_ADMIN));
        User created = service.create(newUser);
        long newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    void duplicateMailCreate() {
        assertThrows(DataIntegrityViolationException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass",
                        Role.ROLE_USER)));
    }

    @Test
    void delete() {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () ->
                service.get(USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void get() {
        User user = service.get(USER_ID);
        USER_MATCHER.assertMatch(user, USER);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getByEmail() {
        User user = service.getByEmail("user@yandex.ru");
        USER_MATCHER.assertMatch(user, USER);
    }

    @Test
    public void getByEmailNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.getByEmail("user@rambler.ru"));
    }

    @Test
    void update() {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        service.update(updated);
        USER_MATCHER.assertMatch(service.get(USER_ID), updated);
    }

    @Test
    void updateNotFound() {
        User updated = new User(USER);
        updated.setEmail("Updated@Name");
        updated.setId(1L);
        assertThrows(NotFoundException.class, () ->
                service.update(updated));
    }

    @Test
    void getAll() {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, ADMIN, USER);
    }

    @Test
    void getWithVotes() {
        User user = service.getWithVotes(USER_ID);
        List<Vote> votes = user.getVotes();
        VOTE_MATCHER.assertMatch(votes, VoteTestData.VOTE_3, VoteTestData.VOTE_1);
    }

    @Test
    void getWithVotesNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.getWithVotes(1));
    }

    @Test
    void enable() {
        service.enable(USER_ID, false);
        assertFalse(service.get(USER_ID).isEnabled());
        service.enable(USER_ID, true);
        assertTrue(service.get(USER_ID).isEnabled());
    }

    @Test
    void createWithValidationException() {
        validateRootCause(() -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "  ", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "V", "mail@yandex.ru", "password", Role.ROLE_USER)), ConstraintViolationException.class);
    }
}