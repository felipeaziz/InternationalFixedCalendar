package org.fixedcalendar;

/**
 * Enumeration representing the days of the week in the International Fixed Calendar.
 * <p>
 * The 7-day week cycle is fixed: every month starts on a Sunday and ends on a Saturday.
 * Intercalary days (Year Day and Leap Day) do not belong to any week and are represented 
 * as {@code INTERCALARY}.
 */
public enum IFCDayOfWeek {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    /** Special day that does not belong to any week (Year Day or Leap Day). */
    INTERCALARY;
    
    private static final IFCDayOfWeek[] ENUMS = values();

    /**
     * Obtains an instance of {@code IFCDayOfWeek} from a day-of-month.
     *
     * @param dayOfMonth the day-of-month, from 1 to 29
     * @return the day-of-week, not null
     */
    public static IFCDayOfWeek ofDayOfMonth(int dayOfMonth) {
        if (dayOfMonth < 1 || dayOfMonth > 29) {
            throw new IllegalArgumentException("Invalid day of month for IFC: " + dayOfMonth);
        }
        if (dayOfMonth == 29) {
            return INTERCALARY;
        }
        return ENUMS[(dayOfMonth - 1) % 7];
    }
}