package ru.cherniak.menuvotingsystem.util;

import ru.cherniak.menuvotingsystem.model.Role;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.to.UserTo;

public class UserUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.ROLE_USER);
    }
    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }
    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

}
