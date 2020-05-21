package ru.cherniak.menuvotingsystem.web;

import ru.cherniak.menuvotingsystem.model.AbstractBase;

public class SecurityUtil {

    private static long id = AbstractBase.START_SEQ;

    private SecurityUtil() {
    }

    public static long authUserId() {
        return id;
    }

    public static void setAuthUserId(long id) {
        SecurityUtil.id = id;
    }


//    public static int authUserCaloriesPerDay() {
//        return DEFAULT_CALORIES_PER_DAY;
//    }
}