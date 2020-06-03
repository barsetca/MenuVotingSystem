package ru.cherniak.menuvotingsystem.repository.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.User;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepository {

    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    @Autowired
    private JpaUserRepository repository;

    @Override
    @Transactional
    public User save(User user) {
        if (!user.isNew() && get(user.id()) == null) {
            return null;
        }
        return repository.save(user);
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        return repository.delete(id) != 0;
    }

    @Override
    public User get(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll(SORT_NAME_EMAIL);
    }

    @Override
    @Transactional
    public User getWithVotes(long id) {
        return repository.findOneWithVotes(id);
    }
}
