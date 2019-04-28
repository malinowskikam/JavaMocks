package validation;

import org.joda.time.LocalTime;

public class Validators {
    private static final String email_regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    public static boolean isNotNull(Object o) {
        return o != null;
    }

    public static boolean isNotEmpty(String s) {
        return !s.equals("");
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
}