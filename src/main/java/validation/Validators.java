package validation;

import models.Reservation;
import models.Restaurant;
import org.joda.time.LocalTime;

public class Validators {
    private static final String email_regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    public static boolean isNotNull(Object o) {
        return o != null;
    }

    public static boolean isNotEmpty(String s) {
        return !"".equals(s);
    }

    public static boolean isEmail(String s) {
        return s.matches(email_regex);
    }

    public static boolean isMultipleOfHalfhour(LocalTime time) {
        return time.getSecondOfMinute() == 0 && time.getMinuteOfHour() % 30 == 0;
    }

    public static boolean isPositiveInteger(int i) {
        return i > 0;
    }

    public static boolean isValidReservationTime(Reservation reservation, Restaurant restaurant)
    {
        boolean isAfterOpen = reservation.getTime().isAfter(restaurant.getOpenHour()) ||
                reservation.getTime().isEqual(restaurant.getOpenHour());

        boolean isBeforeClose = reservation.getTime().isBefore(restaurant.getCloseHour());

        return isAfterOpen && isBeforeClose;
    }
}