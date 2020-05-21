package ru.cherniak.menuvotingsystem.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import ru.cherniak.menuvotingsystem.VoteTestData;
import ru.cherniak.menuvotingsystem.model.Role;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.model.Vote;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.cherniak.menuvotingsystem.UserTestData.*;

class UserServiceTest extends AbstractServiceTest{

    @Autowired
    private UserService service;

    @Test
    void create() throws Exception {
        User newUser = new User(null, "CreateUser", "create@gmail.com", "newPass", Role.ROLE_USER);
        User created = service.create(newUser);
        newUser.setId(created.getId());
        assertMatch(service.getAll(), ADMIN, newUser, USER);
    }

    @Test
    void duplicateMailCreate() throws Exception {
        assertThrows(DataIntegrityViolationException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ROLE_USER)));

    }

    @Test
    void delete() throws Exception {
        service.delete(USER_ID);
        assertMatch(service.getAll(), ADMIN);
    }

//    @Test(expected = NotFoundException.class)
//    public void deletedNotFound() throws Exception {
//        service.delete(1);
//    }

    @Test
    void get() throws Exception {
        User user = service.get(USER_ID);
        assertMatch(user, USER);
    }

//    @Test(expected = NotFoundException.class)
//    public void getNotFound() throws Exception {
//        service.get(1);
//    }

    @Test
    void getByEmail() throws Exception {
        User user = service.getByEmail("user@yandex.ru");
        assertMatch(user, USER);
    }

    @Test
    void update() throws Exception {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        service.update(updated);
        assertMatch(service.get(USER_ID), updated);
    }

    @Test
    void getAll() throws Exception {
        List<User> all = service.getAll();
        assertMatch(all, ADMIN, USER);
    }

    @Test
    void getWithListVotes() {
        User user = service.getWithListVotes(USER_ID);
        List<Vote> votes = user.getVotes();
        VoteTestData.assertMatch(votes, VoteTestData.VOTE_3, VoteTestData.VOTE_1);
    }
}