package ru.cherniak.menuvotingsystem.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.cherniak.menuvotingsystem.AuthorizedUser;

import static java.util.Objects.requireNonNull;

public class SecurityUtil {

    public static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    public static AuthorizedUser get() {
        AuthorizedUser user = safeGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }

    public static long authUserId() {
        return get().getUserTo().id();
    }

    }