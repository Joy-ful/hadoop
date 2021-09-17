package com.hadoop.squenceFiletest;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class test1 {
    public static void main(String[] args) {
        Date nextDays = test1.getNextDays(2);
        Date nextDays1 = test1.getNextDays(3);

        List<String> names = Arrays.asList("Bob","Alice");
        Stream<String> a = Stream.of("A", "B");
        a.forEach(System.out::println);
        System.out.println(nextDays);
        System.out.println(nextDays1);
        String[] array = new String[] { "Apple", "Orange", "Banana", "Lemon" };
        Arrays.sort(array, (s1, s2) -> {
            return s1.compareTo(s2);
        });
        System.out.println(String.join(", ", array));
    }
    public static Date getNextDays(int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, days);
        return c.getTime();
    }
}
