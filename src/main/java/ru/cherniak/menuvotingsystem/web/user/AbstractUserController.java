package ru.cherniak.menuvotingsystem.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.service.UserService;
import ru.cherniak.menuvotingsystem.to.UserTo;
import ru.cherniak.menuvotingsystem.util.UserUtil;

import java.util.List;

import static ru.cherniak.menuvotingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserService service;

    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public User get(long id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public User createTo(UserTo userTo) {
        log.info("createTo {}", userTo);
        return create(UserUtil.createNewFromTo(userTo));
    }

    public void delete(long id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(User user, long id) throws BindException {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public void updateTo(UserTo userTo, long id) {
        log.info("updateTo {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        service.updateTo(userTo);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }

    public User getWithVotes(long id) {
        log.info("getWithVotes {}", id);
        return service.getWithVotes(id);
    }

    public void enable(long id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        service.enable(id, enabled);
    }
}

