package ru.cherniak.menuvotingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.repository.UserRepository;

import java.util.List;

    @Service
    public class UserService {

        protected final Logger log = LoggerFactory.getLogger(getClass());

        private final UserRepository repository;

        @Autowired
        public UserService(UserRepository repository) {
            this.repository = repository;
        }

        public User create(User user) {
            log.info("create {}", user);
            Assert.notNull(user, "user must not be null");
            return repository.save(user);
        }

        public void delete(long id) /*throws NotFoundException*/ {
            log.info("delete {}", id);
            //checkNotFoundWithId(repository.delete(id), id);
            repository.delete(id);
        }

        public User get(long id) /*throws NotFoundException*/{
            log.info("get {}", id);
           // return checkNotFoundWithId(repository.get(id), id);
            return repository.get(id);
        }

        public User getByEmail(String email) /*throws NotFoundException*/ {
            log.info("getByEmail {}", email);
            Assert.notNull(email, "email must not be null");
           // return checkNotFound(repository.getByEmail(email), "email=" + email);
            return repository.getByEmail(email);
        }

        public List<User> getAll() {
            log.info("getAll");
            return repository.getAll();
        }

        public void update(User user) /*throws NotFoundException*/{
            log.info("update {}", user);
            Assert.notNull(user, "user must not be null");
           // checkNotFoundWithId(repository.save(user), user.getId());
           repository.save(user);
        }
    }

