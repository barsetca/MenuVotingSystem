package ru.cherniak.menuvotingsystem;


import ru.cherniak.menuvotingsystem.model.Role;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

import static ru.cherniak.menuvotingsystem.model.AbstractBase.START_SEQ;

public class UserTestData {

    public static TestMatcher<User> USER_MATCHER = TestMatcher.usingFieldsComparator(User.class, "registered", "roles", "votes", "password");

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }

    public static final long USER_ID = START_SEQ;
    public static final long ADMIN_ID = START_SEQ + 1;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, LocalDateTime.now(),
                Collections.singleton(Role.ROLE_USER));
    }

    public static User getUpdated() {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
        return updated;
    }
}
