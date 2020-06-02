package ru.cherniak.menuvotingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.AuthorizedUser;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.repository.user.UserRepository;
import ru.cherniak.menuvotingsystem.to.UserTo;
import ru.cherniak.menuvotingsystem.util.UserUtil;

import java.util.List;

import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFound;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        log.info("create {}", user);
        Assert.notNull(user, "user must not be null");
        return repository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(long id) /*throws NotFoundException*/ {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id), id);

    }

    public User get(long id) /*throws NotFoundException*/ {
        log.info("get {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public User getByEmail(String email) /*throws NotFoundException*/ {
        log.info("getByEmail {}", email);
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email= " + email);

    }

    @Cacheable("users")
    public List<User> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @CacheEvict(value = "users", allEntries = true)
    public void update(User user) {
        log.info("update {}", user);
        Assert.notNull(user, "user must not be null");
        checkNotFound(repository.save(user), "user= " + user);

    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void updateTo(UserTo userTo) {
        User user = get(userTo.id());
        repository.save(UserUtil.updateFromTo(user, userTo));
    }

    public User getWithVotes(long id) {
        log.info("getWithVotes {}", id);
        return checkNotFoundWithId(repository.getWithVotes(id), id);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void enable(long id, boolean enabled) {
        User user = checkNotFoundWithId(get(id), id);
        user.setEnabled(enabled);
        update(user);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }



}


