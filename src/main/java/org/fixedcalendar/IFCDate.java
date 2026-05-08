package org.fixedcalendar;

import java.io.Serial;
import java.io.Serializable;
import java.time.*;
import java.time.chrono.*;
import java.time.temporal.*;
import java.util.Objects;

/**
 * A date in the International Fixed Calendar system.
 * <p>
 * This class is immutable, thread-safe and serializable.
 * It implements {@link ChronoLocalDate} to provide full integration with java.time API.
 */
public final class IFCDate implements ChronoLocalDate, Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;
    private final LocalDate isoDate;

    /**
     * Creates an instance of {@code IFCDate} from a standard {@link LocalDate}.
     * 
     * @param isoDate the ISO-8601 date, not null
     */
    IFCDate(LocalDate isoDate) {
        Objects.requireNonNull(isoDate, "isoDate");
        this.isoDate = isoDate;
    }

    /**
     * Obtains the current date from the system clock in the default time-zone.
     *
     * @return the current IFC date using the system clock and default time-zone, not null
     */
    public static IFCDate now() {
        return from(LocalDate.now());
    }

    /**
     * Obtains the current date from the system clock in the specified time-zone.
     *
     * @param zone  the zone ID to use, not null
     * @return the current IFC date using the system clock, not null
     */
    public static IFCDate now(ZoneId zone) {
        return from(LocalDate.now(zone));
    }

    /**
     * Obtains the current date from the specified clock.
     *
     * @param clock  the clock to use, not null
     * @return the current IFC date, not null
     */
    public static IFCDate now(Clock clock) {
        return from(LocalDate.now(clock));
    }

    /**
     * Obtains an {@code IFCDate} from year, month and day.
     *
     * @param year the year to represent
     * @param month the month-of-year to represent
     * @param dayOfMonth the day-of-month to represent
     * @return the International Fixed date, not null
     * @throws DateTimeException if the date is invalid for the IFC system
     */
    public static IFCDate of(int year, IFCMonth month, int dayOfMonth) {
        Objects.requireNonNull(month, "month");
        
        if (dayOfMonth == 29) {
            boolean isLeap = java.time.Year.isLeap(year);
            if (month == IFCMonth.JUNE && !isLeap) {
                throw new DateTimeException("June only has 29 days in leap years.");
            }
            if (month != IFCMonth.JUNE && month != IFCMonth.DECEMBER) {
                throw new DateTimeException("Only June and December can have 29 days.");
            }
        } else if (dayOfMonth < 1 || dayOfMonth > 28) {
            throw new DateTimeException("Invalid day of month: " + dayOfMonth);
        }

        boolean isLeap = java.time.Year.isLeap(year);
        int doy;
        if (month == IFCMonth.JUNE && dayOfMonth == 29) {
            doy = 169;
        } else if (month == IFCMonth.DECEMBER && dayOfMonth == 29) {
            doy = isLeap ? 366 : 365;
        } else {
            doy = ((month.getValue() - 1) * 28) + dayOfMonth;
            if (isLeap && doy >= 169) {
                doy++;
            }
        }
        return new IFCDate(LocalDate.ofYearDay(year, doy));
    }

    /**
     * Obtains an {@code IFCDate} from a ISO (Gregorian) date.
     *
     * @param gregorianDate the Gregorian date to convert, not null
     * @return the IFC date, not null
     */
    public static IFCDate from(LocalDate gregorianDate) {
        return new IFCDate(gregorianDate);
    }

    /**
     * Gets the chronology of this date, which is the International Fixed Calendar.
     *
     * @return the IFC chronology, not null
     */
    @Override
    public Chronology getChronology() {
        return IFCChronology.INSTANCE;
    }

    /**
     * Gets the era applicable at this date.
     *
     * @return the era, not null
     */
    @Override
    public Era getEra() {
        return IsoEra.CE;
    }

    /**
     * Checks if the year is a leap year according to the IFC rules.
     *
     * @return true if the year is a leap year
     */
    @Override
    public boolean isLeapYear() {
        return isoDate.isLeapYear();
    }

    /**
     * Checks if the specified field is supported by this calendar system.
     *
     * @param field the field to check, null returns false
     * @return true if the field is supported
     */
    @Override
    public boolean isSupported(TemporalField field) {
        return isoDate.isSupported(field);
    }

    /**
     * Gets the value of the specified field as an {@code int}.
     *
     * @param field the field to get, not null
     * @return the value for the field
     */
    @Override
    public int get(TemporalField field) {
        if (field instanceof ChronoField) {
            return (int) getLong(field);
        }
        return (int) field.getFrom(this);
    }

    /**
     * Gets the value of the specified field as a {@code long}.
     *
     * @param field the field to get, not null
     * @return the value for the field
     */
    @Override
    public long getLong(TemporalField field) {
        if (field instanceof ChronoField) {
            switch ((ChronoField) field) {
                case DAY_OF_MONTH: return getDayOfMonth();
                case MONTH_OF_YEAR: return getMonth().getValue();
                case YEAR_OF_ERA:
                case YEAR: return isoDate.getYear();
                case DAY_OF_YEAR: return isoDate.getDayOfYear();
                case PROLEPTIC_MONTH: return (isoDate.getYear() * 13L) + getMonth().getValue() - 1;
                case ERA: return 1;
                default: return isoDate.getLong(field);
            }
        }
        return field.getFrom(this);
    }

    /**
     * Gets the range of valid values for the specified field.
     *
     * @param field the field to query the range for, not null
     * @return the range of valid values for the field, not null
     */
    @Override
    public ValueRange range(TemporalField field) {
        if (field instanceof ChronoField f) {
            if (isSupported(field)) {
                switch (f) {
                    case DAY_OF_MONTH: return ValueRange.of(1, lengthOfMonth());
                    case MONTH_OF_YEAR: return ValueRange.of(1, 13);
                    case YEAR_OF_ERA: return ChronoField.YEAR.range();
                }
            }
        }
        return ChronoLocalDate.super.range(field);
    }

    /**
     * Returns an object of the same type as this object with the specified field altered.
     *
     * @param field the field to set in the result, not null
     * @param newValue the new value of the field in the result
     * @return an object of the same type with the specified field set, not null
     */
    @Override
    public IFCDate with(TemporalField field, long newValue) {
        if (field instanceof ChronoField) {
            return IFCDate.from(isoDate.with(field, newValue));
        }
        return (IFCDate) field.adjustInto(this, newValue);
    }

    /**
     * Returns an adjusted object of the same type as this object with the adjustment made.
     *
     * @param adjuster the adjuster to use, not null
     * @return an object of the same type with the specified adjustment made, not null
     */
    @Override
    public IFCDate with(TemporalAdjuster adjuster) {
        return (IFCDate) ChronoLocalDate.super.with(adjuster);
    }

    /**
     * Returns the epoch day count for this date.
     *
     * @return the epoch day count
     */
    @Override
    public long toEpochDay() {
        return isoDate.toEpochDay();
    }

    /**
     * Returns the length of the month represented by this date.
     * In the IFC, months are 28 days long, except for Leap Day and Year Day months which can have 29.
     *
     * @return the length of the month in days
     */
    @Override
    public int lengthOfMonth() {
        if (isLeapDay()) return 29;
        if (isYearDay()) return 29;
        return 28;
    }

    /**
     * Returns the length of the year represented by this date.
     *
     * @return 366 if leap year, 365 otherwise
     */
    @Override
    public int lengthOfYear() {
        return isoDate.lengthOfYear();
    }

    /**
     * Returns an object of the same type as this object with an amount added.
     *
     * @param amount the amount of the specified unit to add, not null
     * @return an object of the same type with the specified period added, not null
     */
    @Override
    public IFCDate plus(TemporalAmount amount) {
        return (IFCDate) ChronoLocalDate.super.plus(amount);
    }

    /**
     * Returns an object of the same type as this object with the specified period added.
     *
     * @param amountToAdd the amount of the unit to add to the result, may be negative
     * @param unit the unit of the amount to add, not null
     * @return an object of the same type with the specified period added, not null
     */
    @Override
    public IFCDate plus(long amountToAdd, TemporalUnit unit) {
        if (unit instanceof ChronoUnit) {
            switch ((ChronoUnit) unit) {
                case MONTHS:
                    return plusDays(Math.multiplyExact(amountToAdd, 28));
                case WEEKS:
                    return plusDays(Math.multiplyExact(amountToAdd, 7));
                case YEARS:
                    return from(isoDate.plusYears(amountToAdd));
                default:
                    return new IFCDate(isoDate.plus(amountToAdd, unit));
            }
        }
        return from(unit.addTo(isoDate, amountToAdd));
    }

    /**
     * Returns an object of the same type as this object with an amount subtracted.
     *
     * @param amount the amount of the specified unit to subtract, not null
     * @return an object of the same type with the specified period subtracted, not null
     */
    @Override
    public IFCDate minus(TemporalAmount amount) {
        return (IFCDate) ChronoLocalDate.super.minus(amount);
    }

    /**
     * Returns an object of the same type as this object with the specified period subtracted.
     *
     * @param amountToSubtract the amount of the unit to subtract from the result, may be negative
     * @param unit the unit of the amount to subtract, not null
     * @return an object of the same type with the specified period subtracted, not null
     */
    @Override
    public IFCDate minus(long amountToSubtract, TemporalUnit unit) {
        if (amountToSubtract == Long.MIN_VALUE) {
            return plus(Long.MAX_VALUE, unit).plus(1, unit);
        }
        return (IFCDate) ChronoLocalDate.super.minus(amountToSubtract, unit);
    }

    /**
     * Combines this date with a time to create an {@code IFCLocalDateTime}.
     *
     * @param localTime the local time to combine with, not null
     * @return the local date-time, not null
     */
    @Override
    public ChronoLocalDateTime<IFCDate> atTime(LocalTime localTime) {
        return IFCLocalDateTime.of(this, localTime);
    }

    /**
     * Calculates the period between this date and another date as a {@code ChronoPeriod}.
     *
     * @param endDate the end date, exclusive, which is converted to an {@code IFCDate}, not null
     * @return the period between this date and the end date, not null
     */
    @Override
    public ChronoPeriod until(ChronoLocalDate endDate) {
        IFCDate end = (IFCDate) getChronology().date(endDate);

        long totalMonths = (end.getYear() - this.getYear()) * 13L +
                          (end.getMonth().getValue() - this.getMonth().getValue());

        int days = end.getDayOfMonth() - this.getDayOfMonth();

        if (totalMonths > 0 && days < 0) {
            totalMonths--;
            days += 28;
        }

        return getChronology().period(Math.toIntExact(totalMonths / 13), Math.toIntExact(totalMonths % 13), days);
    }

    /**
     * Calculates the amount of time until another date-time in terms of a single unit.
     *
     * @param endExclusive the end date-time, exclusive, which is converted to a {@code Temporal}, not null
     * @param unit the unit to measure the amount in, not null
     * @return the amount of time between this date-time and the end date-time
     */
    @Override
    public long until(Temporal endExclusive, TemporalUnit unit) {
        return isoDate.until(endExclusive, unit);
    }

    /**
     * Returns a copy of this date with the specified number of days added.
     *
     * @param days the days to add, may be negative
     * @return an IFCDate with the days added, not null
     */
    public IFCDate plusDays(long days) {
        return new IFCDate(isoDate.plusDays(days));
    }

    /**
     * Gets the proleptic year field.
     *
     * @return the proleptic year
     */
    public int getYear() {
        return isoDate.getYear();
    }

    /**
     * Gets the International Fixed Calendar month enumeration.
     *
     * @return the month enumeration, not null
     */
    public IFCMonth getMonth() {
        if(isYearDay()) {
            return IFCMonth.DECEMBER;
        }
        if(isLeapDay()) {
            return IFCMonth.JUNE;
        }
        int doy = getAdjustedDayOfYear();
        return IFCMonth.of(((doy - 1) / 28) + 1);
    }

    /**
     * Gets the day-of-month field.
     *
     * @return the day-of-month, from 1 to 29
     */
    public int getDayOfMonth() {
        int doy = getAdjustedDayOfYear();
        if (isLeapDay() || isYearDay()) return 29;
        return ((doy - 1) % 28) + 1;
    }

    /**
     * Returns the day of year adjusted for the leap day shift.
     *
     * @return the adjusted day of year
     */
    private int getAdjustedDayOfYear() {
        int doy = isoDate.getDayOfYear();
        if (isoDate.isLeapYear()) {
            if (doy == 169) return 169;
            if (doy > 169) doy--;
        }
        return doy;
    }

    /**
     * Checks if the date is the Leap Day (June 29th).
     *
     * @return true if this date is a Leap Day
     */
    public boolean isLeapDay() {
        return isoDate.isLeapYear() && isoDate.getDayOfYear() == 169;
    }

    /**
     * Checks if the date is the Year Day (December 29th).
     *
     * @return true if this date is a Year Day
     */
    public boolean isYearDay() {
        return isoDate.getDayOfYear() == (isoDate.isLeapYear() ? 366 : 365);
    }

    /**
     * Converts this date to a {@link LocalDate} (ISO Calendar).
     *
     * @return the Gregorian date, not null
     */
    public LocalDate toLocalDate() {
        return isoDate;
    }

    /**
     * Checks if this date is equal to another date.
     *
     * @param o the object to check, null returns false
     * @return true if this is equal to the other date
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof IFCDate && isoDate.equals(((IFCDate) o).isoDate));
    }

    /**
     * Compares this date to another date.
     *
     * @param other the other date to compare to, not null
     * @return the comparator value, negative if less, positive if greater
     */
    @Override
    public int compareTo(ChronoLocalDate other) {
        return ChronoLocalDate.super.compareTo(other);
    }

    /**
     * A hash code for this date.
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return isoDate.hashCode();
    }

    /**
     * Outputs this date as a {@code String}, such as {@code 2023-SOL-01}.
     *
     * @return a string representation of this date, not null
     */
    @Override
    public String toString() {
        return String.format("%04d-%s-%02d", getYear(), getMonth().name(), getDayOfMonth());
    }

    /**
     * Gets the day of week for this date.
     * In the IFC, days 1-28 follow a fixed 7-day week cycle (1=Sunday, 7=Saturday).
     * Intercalary days (Leap Day and Year Day) are outside the standard week.
     *
     * @return the day of the week, not null
     */
    public IFCDayOfWeek getDayOfWeek() {
        if(isLeapDay() || isYearDay()) {
            return IFCDayOfWeek.INTERCALARY;
        }
        return IFCDayOfWeek.ofDayOfMonth(getDayOfMonth());
    }
}
