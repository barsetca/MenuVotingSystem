package ru.cherniak.menuvotingsystem;


import org.springframework.test.web.servlet.ResultMatcher;
import ru.cherniak.menuvotingsystem.model.Role;
import ru.cherniak.menuvotingsystem.model.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.cherniak.menuvotingsystem.model.AbstractBase.START_SEQ;
import static ru.cherniak.menuvotingsystem.TestUtil.readFromJsonMvcResult;
import static ru.cherniak.menuvotingsystem.TestUtil.readListFromJsonMvcResult;

public class UserTestData {

    public static TestMatcher<User> USER_MATCHER = TestMatcher.usingFieldsComparator(User.class, "registered", "roles", "votes");

    public static final long USER_ID = START_SEQ;
    public static final long ADMIN_ID = START_SEQ + 1;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN);

   }
