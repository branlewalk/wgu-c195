package org.branlewalk.ui;

import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WeekDaysTest {

    @Test
    public void getCurrent() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.JULY,16);
        List<Integer> currentDay = new WeekDays(calendar).getCurrent();
        assertThat(currentDay, is(Arrays.asList(14,15,16,17,18,19,20)));
    }

    @Test
    public void currentStraddleMonth1() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.JULY,1);
        List<Integer> currentDay = new WeekDays(calendar).getCurrent();
        assertThat(currentDay, is(Arrays.asList(30,1,2,3,4,5,6)));
    }

    @Test
    public void currentStraddleMonth2() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.JULY,31);
        List<Integer> currentDay = new WeekDays(calendar).getCurrent();
        assertThat(currentDay, is(Arrays.asList(28,29,30,31,1,2,3)));
    }
}