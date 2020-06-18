package ru.cherniak.menuvotingsystem;

import ru.cherniak.menuvotingsystem.model.AbstractBaseNameId;
import ru.cherniak.menuvotingsystem.model.Dish;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.time.LocalDate.of;
import static ru.cherniak.menuvotingsystem.model.AbstractBase.START_SEQ;

public class DishTestData {
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsComparator(Dish.class, "restaurant");

    public static final Comparator<Dish> DISH_COMPARATOR = Comparator.comparing(AbstractBaseNameId::getName);

    public static final long DISH_ID = START_SEQ + 4;

    public static final LocalDate DATE_290420 = of(2020, Month.APRIL, 29);
    public static final LocalDate DATE_300420 = of(2020, Month.APRIL, 30);

    public static final Dish DISH_1 = new Dish(DISH_ID, "БигМак", DATE_290420, 500);
    public static final Dish DISH_2 = new Dish(DISH_ID + 1, "Кола", DATE_290420, 100);
    public static final Dish DISH_3 = new Dish(DISH_ID + 2, "Цезарь", DATE_290420, 400);
    public static final Dish DISH_4 = new Dish(DISH_ID + 3, "Вино", DATE_290420, 200);
    public static final Dish DISH_5 = new Dish(DISH_ID + 4, "Нагетсы", DATE_300420, 200);
    public static final Dish DISH_6 = new Dish(DISH_ID + 5, "Цезарь", DATE_300420, 300);
    public static final Dish DISH_7 = new Dish(DISH_ID + 6, "Пицца", DATE_300420, 400);
    public static final Dish DISH_8 = new Dish(DISH_ID + 7, "Гаспачо", DATE_300420, 300);

    public static final List<Dish> ALL_DISHES_R1 = List.of(DISH_5, DISH_6, DISH_1, DISH_2);
    public static final List<Dish> ALL_DISHES_R2 = List.of(DISH_8, DISH_7, DISH_4, DISH_3);
    public static final Collection<Dish> ALL_DISHES = List.of(DISH_5, DISH_6, DISH_8, DISH_7, DISH_1, DISH_2, DISH_4, DISH_3);


    public static Dish getCreatedToday() {
        return new Dish(null, "БигМак", LocalDate.now(), 300);
    }

    public static Dish getUpdated(Dish dish) {
        return new Dish(dish.getId(), "Обновленный пункт", dish.getDate(), 200);
    }


}
