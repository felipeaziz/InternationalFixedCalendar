package org.fixedcalendar;

import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Enumeration representing the 13 months of the International Fixed Calendar.
 * <p>
 * Each month consists of exactly 28 days, except June (in leap years)
 * and December, which can contain a 29th intercalary day.
 */
public enum IFCMonth {
    /** January */
    JANUARY(1),
    /** February */
    FEBRUARY(2),
    /** March */
    MARCH(3),
    /** April */
    APRIL(4),
    /** May */
    MAY(5),
    /** June */
    JUNE(6),
    /** Sol - The extra month between June and July */
    SOL(7),
    /** July */
    JULY(8),
    /** August */
    AUGUST(9),
    /** September */
    SEPTEMBER(10),
    /** October */
    OCTOBER(11),
    /** November */
    NOVEMBER(12),
    /** December */
    DECEMBER(13);

    private final int value;

    IFCMonth(int value) {
        this.value = value;
    }

    /**
     * Gets the month-of-year int value.
     *
     * @return the month-of-year, from 1 to 13
     */
    public int getValue() {
        return value;
    }

    /**
     * Obtains an instance of {@code IFCMonth} from an int value.
     *
     * @param monthValue the month-of-year to represent, from 1 to 13
     * @return the month-of-year, not null
     * @throws IllegalArgumentException if the monthValue is invalid
     */
    public static IFCMonth of(int monthValue) {
        if (monthValue < 1 || monthValue > 13) {
            throw new IllegalArgumentException("Month must be between 1 and 13");
        }
        return values()[monthValue - 1];
    }

    /**
     * Gets the textual representation, such as 'January' or 'Sol'.
     *
     * @param style  the length of the text required, not null
     * @param locale the locale to use, not null
     * @return the text value of the month, not null
     */
    public String getDisplayName(TextStyle style, Locale locale) {
        String name = name();
        String formatted = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        if (style == TextStyle.SHORT || style == TextStyle.NARROW) {
            return formatted.substring(0, Math.min(formatted.length(), 3));
        }
        return formatted;
    }

    /**
     * Returns the standard length of the month in days.
     *
     * @return the standard length of 28 days
     */
    public int lengthOfMonth() {
        return 28;
    }
}
