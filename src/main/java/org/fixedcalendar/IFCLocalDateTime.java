package org.fixedcalendar;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

/**
 * A date-time without a time-zone in the International Fixed Calendar system.
 * <p>
 * This class combines an {@link IFCDate} and a {@link LocalTime}.
 * <p>
 * This class is immutable and thread-safe.
 */
public final class IFCLocalDateTime implements ChronoLocalDateTime<IFCDate>, Temporal, TemporalAdjuster, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final IFCDate date;
    private final LocalTime time;

    /**
     * Obtains an instance of {@code IFCLocalDateTime} from date and time.
     *
     * @param date the local date, not null
     * @param time the local time, not null
     * @return the local date-time, not null
     */
    public static IFCLocalDateTime of(IFCDate date, LocalTime time) {
        return new IFCLocalDateTime(date, time);
    }

    /**
     * Private constructor.
     * 
     * @param date the local date, not null
     * @param time the local time, not null
     */
    private IFCLocalDateTime(IFCDate date, LocalTime time) {
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(time, "time");
        this.date = date;
        this.time = time;
    }

    /**
     * Gets the {@code IFCDate} part of this date-time.
     * 
     * @return the date part, not null
     */
    @Override
    public IFCDate toLocalDate() {
        return date;
    }

    /**
     * Gets the {@code LocalTime} part of this date-time.
     * 
     * @return the time part, not null
     */
    @Override
    public LocalTime toLocalTime() {
        return time;
    }

    /**
     * Gets the chronology of this date-time.
     * 
     * @return the {@code IFCChronology} instance, not null
     */
    @Override
    public org.fixedcalendar.IFCChronology getChronology() {
        return IFCChronology.INSTANCE;
    }

    /**
     * Returns an adjusted object of the same type as this object with the adjustment made.
     * 
     * @param adjuster the adjuster to use, not null
     * @return an {@code IFCLocalDateTime} based on this date-time with the adjustment made, not null
     */
    @Override
    public IFCLocalDateTime with(TemporalAdjuster adjuster) {
        if (adjuster instanceof IFCDate) {
            return new IFCLocalDateTime((IFCDate) adjuster, time);
        }
        if (adjuster instanceof LocalTime) {
            return new IFCLocalDateTime(date, (LocalTime) adjuster);
        }
        if (adjuster instanceof IFCLocalDateTime) {
            return (IFCLocalDateTime) adjuster;
        }
        return (IFCLocalDateTime) adjuster.adjustInto(this);
    }

    /**
     * Returns an object of the same type as this object with the specified field altered.
     * 
     * @param field the field to set in the result, not null
     * @param newValue the new value of the field in the result
     * @return an {@code IFCLocalDateTime} based on this date-time with the specified field set, not null
     */
    @Override
    public IFCLocalDateTime with(TemporalField field, long newValue) {
        if (field instanceof ChronoField) {
            if (field.isDateBased()) {
                return of(date.with(field, newValue), time);
            } else {
                return of(date, time.with(field, newValue));
            }
        }
        return (IFCLocalDateTime) field.adjustInto(this, newValue);
    }

    /**
     * Returns an object of the same type as this object with the specified period added.
     * 
     * @param amountToAdd the amount of the unit to add to the result, may be negative
     * @param unit the unit of the amount to add, not null
     * @return an {@code IFCLocalDateTime} based on this date-time with the specified period added, not null
     */
    @Override
    public IFCLocalDateTime plus(long amountToAdd, TemporalUnit unit) {
        if (unit instanceof ChronoUnit) {
            if (unit.isDateBased()) {
                return of(date.plus(amountToAdd, unit), time);
            } else {
                return of(date, time.plus(amountToAdd, unit));
            }
        }
        return (IFCLocalDateTime) unit.addTo(this, amountToAdd);
    }

    /**
     * Returns an object of the same type as this object with the specified period subtracted.
     * 
     * @param amountToSubtract the amount of the unit to subtract from the result, may be negative
     * @param unit the unit of the amount to subtract, not null
     * @return an {@code IFCLocalDateTime} based on this date-time with the specified period subtracted, not null
     */
    @Override
    public IFCLocalDateTime minus(long amountToSubtract, TemporalUnit unit) {
        return plus(-amountToSubtract, unit);
    }

    /**
     * Calculates the amount of time until another date-time in terms of a single unit.
     * 
     * @param endExclusive the end date-time, exclusive, not null
     * @param unit the unit to measure the amount in, not null
     * @return the amount of time between this date-time and the end date-time
     */
    @Override
    public long until(Temporal endExclusive, TemporalUnit unit) {
        return toLocalDateTime().until(endExclusive, unit);
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
            if (field.isDateBased()) {
                return date.getLong(field);
            } else {
                return time.getLong(field);
            }
        }
        return field.getFrom(this);
    }

    /**
     * Combines this date-time with a time-zone to create an {@code IFCZonedDateTime}.
     * 
     * @param zone the zone ID to use, not null
     * @return the zoned date-time, not null
     */
    @Override
    public ChronoZonedDateTime<IFCDate> atZone(ZoneId zone) {
        return IFCZonedDateTime.of(this, zone);
    }

    /**
     * Converts this date-time to a standard {@link LocalDateTime}.
     * 
     * @return the equivalent ISO-8601 local date-time, not null
     */
    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(date.toLocalDate(), time);
    }

    /**
     * Checks if this date-time is equal to another date-time.
     * 
     * @param o the object to check, null returns false
     * @return true if this is equal to the other date-time
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IFCLocalDateTime that = (IFCLocalDateTime) o;
        return Objects.equals(date, that.date) && Objects.equals(time, that.time);
    }

    /**
     * A hash code for this date-time.
     * 
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(date, time);
    }

    /**
     * Outputs this date-time as a {@code String}, such as {@code 2023-JANUARY-01T10:30}.
     * 
     * @return a string representation of this date-time, not null
     */
    @Override
    public String toString() {
        return date + "T" + time;
    }

    /**
     * Adjusts the specified temporal object to have the same date and time as this object.
     * 
     * @param temporal the target object to be adjusted, not null
     * @return the adjusted object, not null
     */
    @Override
    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.EPOCH_DAY, toEpochDay())
                .with(ChronoField.NANO_OF_DAY, time.toNanoOfDay());
    }

    /**
     * Returns the epoch day count for the date part.
     * 
     * @return the epoch day count
     */
    private long toEpochDay() {
        return date.toEpochDay();
    }

    /**
     * Checks if the specified field is supported.
     * 
     * @param field the field to check, null returns false
     * @return true if the field is supported
     */
    @Override
    public boolean isSupported(TemporalField field) {
        return date.isSupported(field) || time.isSupported(field);
    }
}
