package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> list = new ArrayList<>();
        int calories = 0;
        Boolean isExcess = null;
        for (UserMeal userMeal: meals) {
            if ((userMeal.getDateTime().toLocalTime().compareTo(startTime)) >= 0 && (userMeal.getDateTime().toLocalTime().compareTo(endTime) < 0)) {
                calories += userMeal.getCalories();
                list.add(new UserMealWithExcess(userMeal.getDateTime(),userMeal.getDescription(),userMeal.getCalories(),isExcess));
            }
        }
        isExcess = calories > caloriesPerDay;

        return list;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Boolean isExcess = null;
        final Integer[] calories = {0};
        Boolean finalIsExcess = isExcess;
        List<UserMealWithExcess> list = meals.stream()
                .filter(userMeal -> (userMeal.getDateTime().toLocalTime().isAfter(startTime)) && (userMeal.getDateTime().toLocalTime().isBefore(endTime)))
                .peek(x -> calories[0] +=x.getCalories())
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(),userMeal.getDescription(),userMeal.getCalories(), finalIsExcess))
                .collect(Collectors.toList());
        isExcess = calories[0] > caloriesPerDay;
        return list;
    }
}
