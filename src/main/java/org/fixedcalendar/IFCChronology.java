package org.fixedcalendar;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.ZoneId;
import java.time.chrono.AbstractChronology;
import java.time.chrono.Era;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import java.time.chrono.IsoEra;
import java.time.chrono.ChronoPeriod;
import java.util.Collections;
import java.util.List;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.io.ObjectStreamException;
import java.time.temporal.TemporalAccessor;

/**
 * Chronology for the International Fixed Calendar.
 * <p>
 * The International Fixed Calendar is a solar calendar proposal for calendar reform 
 * designed by Moses B. Cotsworth, who presented it in 1902. It provides for a year 
 * of 13 months of 28 days each, with one or two intercalary days.
 * <p>
 * This class is immutable and thread-safe.
 */
public final class IFCChronology extends AbstractChronology implements Serializable {

    /**
     * Singleton instance of the International Fixed Chronology.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The singleton instance of {@code IFCChronology}.
     */
    public static final IFCChronology INSTANCE = new IFCChronology();

    /**
     * Private constructor to ensure singleton.
     */
    private IFCChronology() {}

    /**
     * Gets the ID of the chronology - 'IFC'.
     *
     * @return the chronology ID, not null
     */
    @Override
    public String getId() {
        return "IFC";
    }

    /**
     * Gets the calendar type of the underlying calendar system - 'ifc'.
     *
     * @return the calendar type, not null
     */
    @Override
    public String getCalendarType() {
        return "ifc";
    }

    /**
     * Obtains an {@code IFCDate} in this chronology from the era, year-of-era,
     * month-of-year and day-of-month fields.
     *
     * @param era the era, not null
     * @param yearOfEra the year-of-era
     * @param month the month-of-year, from 1 to 13
     * @param dayOfMonth the day-of-month, from 1 to 29
     * @return the IFC date, not null
     * @throws java.time.DateTimeException if the date is invalid
     */
    @Override
    public IFCDate date(Era era, int yearOfEra, int month, int dayOfMonth) {
        return date(prolepticYear(era, yearOfEra), month, dayOfMonth);
    }

    /**
     * Obtains an {@code IFCDate} in this chronology from the proleptic-year,
     * month-of-year and day-of-month fields.
     *
     * @param prolepticYear the proleptic-year
     * @param month the month-of-year, from 1 to 13
     * @param dayOfMonth the day-of-month, from 1 to 29
     * @return the IFC date, not null
     * @throws java.time.DateTimeException if the date is invalid
     */
    @Override
    public IFCDate date(int prolepticYear, int month, int dayOfMonth) {
        return IFCDate.of(prolepticYear, IFCMonth.of(month), dayOfMonth);
    }

    /**
     * Obtains an {@code IFCDate} in this chronology from the era, year-of-era
     * and day-of-year fields.
     *
     * @param era the era, not null
     * @param yearOfEra the year-of-era
     * @param dayOfYear the day-of-year, from 1 to 366
     * @return the IFC date, not null
     * @throws java.time.DateTimeException if the date is invalid
     */
    @Override
    public IFCDate dateYearDay(Era era, int yearOfEra, int dayOfYear) {
        return dateYearDay(prolepticYear(era, yearOfEra), dayOfYear);
    }

    /**
     * Obtains an {@code IFCDate} in this chronology from the proleptic-year
     * and day-of-year fields.
     *
     * @param prolepticYear the proleptic-year
     * @param dayOfYear the day-of-year, from 1 to 366
     * @return the IFC date, not null
     * @throws java.time.DateTimeException if the date is invalid
     */
    @Override
    public IFCDate dateYearDay(int prolepticYear, int dayOfYear) {
        return IFCDate.from(LocalDate.ofYearDay(prolepticYear, dayOfYear));
    }

    /**
     * Obtains the current {@code IFCDate} in this chronology from the system clock
     * in the default time-zone.
     *
     * @return the current IFC date, not null
     */
    @Override
    public IFCDate dateNow() {
        return dateNow(Clock.systemDefaultZone());
    }

    /**
     * Obtains the current {@code IFCDate} in this chronology from the system clock
     * in the specified time-zone.
     *
     * @param zone the zone ID to use, not null
     * @return the current IFC date, not null
     */
    @Override
    public IFCDate dateNow(ZoneId zone) {
        return dateNow(Clock.system(zone));
    }

    /**
     * Obtains the current {@code IFCDate} in this chronology from the specified clock.
     *
     * @param clock the clock to use, not null
     * @return the current IFC date, not null
     */
    @Override
    public IFCDate dateNow(Clock clock) {
        return IFCDate.from(LocalDate.now(clock));
    }

    /**
     * Obtains an {@code IFCDate} in this chronology from the epoch-day.
     *
     * @param epochDay the epoch day, from 0000-01-01 (ISO)
     * @return the IFC date, not null
     * @throws java.time.DateTimeException if the epoch day is invalid
     */
    @Override
    public IFCDate dateEpochDay(long epochDay) {
        return IFCDate.from(LocalDate.ofEpochDay(epochDay));
    }

    /**
     * Checks if the specified year is a leap year.
     *
     * @param prolepticYear the year to check
     * @return true if the year is a leap year
     */
    @Override
    public boolean isLeapYear(long prolepticYear) {
        return java.time.Year.isLeap(prolepticYear);
    }

    /**
     * Calculates the proleptic-year from the era and year-of-era.
     * For IFC, the proleptic year is the same as the year-of-era.
     *
     * @param era the era, not null
     * @param yearOfEra the year-of-era
     * @return the proleptic-year
     */
    @Override
    public int prolepticYear(Era era, int yearOfEra) {
        return yearOfEra;
    }

    /**
     * Creates the era object corresponding to the numeric value.
     * For IFC, this delegates to {@link IsoEra}.
     *
     * @param eraValue the era value
     * @return the era, not null
     */
    @Override
    public Era eraOf(int eraValue) {
        return IsoEra.of(eraValue);
    }

    /**
     * Gets the list of eras in this chronology.
     * For IFC, this returns a list containing only {@link IsoEra#CE}.
     *
     * @return the list of eras, not null
     */
    @Override
    public List<Era> eras() {
        return Collections.singletonList(IsoEra.CE);
    }

    /**
     * Gets the range of valid values for the specified field.
     *
     * @param field the field to query the range for, not null
     * @return the range of valid values for the field, not null
     */
    @Override
    public ValueRange range(ChronoField field) {
        switch (field) {
            case MONTH_OF_YEAR:
                return ValueRange.of(1, 13);
            case DAY_OF_MONTH:
                return ValueRange.of(1, 29);
            default:
                return field.range();
        }
    }

    /**
     * Obtains an {@code IFCLocalDateTime} in this chronology from another temporal object.
     *
     * @param temporal the temporal object to convert, not null
     * @return the IFC local date-time, not null
     * @throws java.time.DateTimeException if unable to convert to an {@code IFCLocalDateTime}
     */
    @Override
    public ChronoLocalDateTime<IFCDate> localDateTime(TemporalAccessor temporal) {
        return IFCLocalDateTime.of(IFCDate.from(LocalDate.from(temporal)), LocalTime.from(temporal));
    }

    /**
     * Obtains an {@code IFCZonedDateTime} in this chronology from another temporal object.
     *
     * @param temporal the temporal object to convert, not null
     * @return the IFC zoned date-time, not null
     * @throws java.time.DateTimeException if unable to convert to an {@code IFCZonedDateTime}
     */
    @Override
    public ChronoZonedDateTime<IFCDate> zonedDateTime(TemporalAccessor temporal) {
        ZonedDateTime zdt = ZonedDateTime.from(temporal);
        return IFCZonedDateTime.of(IFCLocalDateTime.of(IFCDate.from(zdt.toLocalDate()), zdt.toLocalTime()), zdt.getZone());
    }

    /**
     * Obtains an {@code IFCZonedDateTime} in this chronology from an {@code Instant}.
     *
     * @param instant the instant to convert, not null
     * @param zone the zone ID to use, not null
     * @return the IFC zoned date-time, not null
     * @throws java.time.DateTimeException if unable to convert to an {@code IFCZonedDateTime}
     */
    @Override
    public ChronoZonedDateTime<IFCDate> zonedDateTime(Instant instant, ZoneId zone) {
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, zone);
        return IFCZonedDateTime.of(IFCLocalDateTime.of(IFCDate.from(ldt.toLocalDate()), ldt.toLocalTime()), zone);
    }

    /**
     * Obtains an {@code IFCPeriod} for this chronology based on years, months and days.
     *
     * @param years the number of years
     * @param months the number of months
     * @param days the number of days
     * @return the IFC period, not null
     */
    @Override
    public ChronoPeriod period(int years, int months, int days) {
        return new IFCPeriod(years, months, days);
    }

    /**
     * Obtains an {@code IFCDate} in this chronology from another temporal object.
     *
     * @param temporal the temporal object to convert, not null
     * @return the IFC date, not null
     * @throws java.time.DateTimeException if unable to convert to an {@code IFCDate}
     */
    @Override
    public IFCDate date(TemporalAccessor temporal) {
        return IFCDate.from(LocalDate.from(temporal));
    }

    /**
     * Ensures the singleton instance during deserialization.
     *
     * @return the singleton instance
     * @throws ObjectStreamException if an error occurs during deserialization
     */
    private Object readResolve() throws ObjectStreamException {
        return INSTANCE;
    }
}
