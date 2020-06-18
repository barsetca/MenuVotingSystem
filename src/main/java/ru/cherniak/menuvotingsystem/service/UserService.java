package ru.cherniak.menuvotingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.AuthorizedUser;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.repository.user.JpaUserRepository;
import ru.cherniak.menuvotingsystem.to.UserTo;
import ru.cherniak.menuvotingsystem.util.UserUtil;

import java.util.List;

import static ru.cherniak.menuvotingsystem.util.UserUtil.prepareToSave;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFound;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final JpaUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(JpaUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return prepareAndSave(user);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        checkNotFound(prepareAndSave(user), "user= " + user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void updateTo(UserTo userTo) {
        User user = get(userTo.id());
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void delete(long id) /*throws NotFoundException*/ {
        checkNotFoundWithId(userRepository.delete(id) != 0, id);
    }

    public User get(long id) /*throws NotFoundException*/ {
        return checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
    }

    public User getByEmail(String email) /*throws NotFoundException*/ {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(userRepository.getByEmail(email), "email= " + email);
    }

    @Cacheable("users")
    public List<User> getAll() {
        return userRepository.findAll(SORT_NAME_EMAIL);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void enable(long id, boolean enabled) {
        User user = checkNotFoundWithId(get(id), id);
        user.setEnabled(enabled);
        save(user);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }

    private User prepareAndSave(User user) {
        return save(prepareToSave(user, passwordEncoder));
    }

    private User save(User user) {
        if (!user.isNew() && get(user.id()) == null) {
            return null;
        }
        return userRepository.save(user);
    }
}


