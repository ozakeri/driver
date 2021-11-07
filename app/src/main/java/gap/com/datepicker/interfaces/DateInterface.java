package gap.com.datepicker.interfaces;

import gap.com.datepicker.components.DateItem;

/**
 * Created by Alireza Afkar on 2/5/16 AD.
 */
public interface DateInterface {
    void setDay(int day);

    void setDay(int day, int month, int year);

    void setMonth(int month);

    void setYear(int year);

    void setDate(int day, int month, int year);

    int getDay();

    int getMonth();

    int getYear();

    int getCurrentYear();

    String[] getWeekDays();

    String[] getMonths();

    DateItem getDateItem();
}
