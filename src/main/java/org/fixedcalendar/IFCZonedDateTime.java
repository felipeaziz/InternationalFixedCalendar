package org.fixedcalendar;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.*;
import java.util.Objects;

/**
 * A date-time with a time-zone in the International Fixed Calendar system.
 * <p>
 * This class combines an {@link IFCLocalDateTime} with a {@link ZoneId}.
 * <p>
 * This class is immutable and thread-safe.
 */
public final class IFCZonedDateTime implements ChronoZonedDateTime<IFCDate>, Temporal, TemporalAdjuster, Serializable {

    private static final long serialVersionUID = 1L;

    private final IFCLocalDateTime dateTime;
    private final ZoneId zone;
    private final ZoneOffset offset;

    /**
     * Obtains an instance of {@code IFCZonedDateTime} from local date-time and zone.
     *
     * @param dateTime the local date-time, not null
     * @param zone the zone ID, not null
     * @return the zoned date-time, not null
     */
    public static IFCZonedDateTime of(IFCLocalDateTime dateTime, ZoneId zone) {
        ZoneOffset offset = zone.getRules().getOffset(dateTime.toLocalDateTime());
        return new IFCZonedDateTime(dateTime, zone, offset);
    }

    /**
     * Private constructor.
     * 
     * @param dateTime the local date-time, not null
     * @param zone the zone ID, not null
     * @param offset the zone offset, not null
     */
    private IFCZonedDateTime(IFCLocalDateTime dateTime, ZoneId zone, ZoneOffset offset) {
        Objects.requireNonNull(dateTime, "dateTime");
        Objects.requireNonNull(zone, "zone");
        Objects.requireNonNull(offset, "offset");
        this.dateTime = dateTime;
        this.zone = zone;
        this.offset = offset;
    }

    /**
     * Gets the chronology of this zoned date-time.
     * 
     * @return the {@code IFCChronology} instance, not null
     */
    @Override
    public org.fixedcalendar.IFCChronology getChronology() {
        return IFCChronology.INSTANCE;
    }

    /**
     * Gets the {@code IFCLocalDateTime} part of this zoned date-time.
     * 
     * @return the local date-time part, not null
     */
    @Override
    public IFCLocalDateTime toLocalDateTime() {
        return dateTime;
    }

    /**
     * Gets the zone offset, such as '+01:00'.
     * 
     * @return the zone offset, not null
     */
    @Override
    public ZoneOffset getOffset() {
        return offset;
    }

    /**
     * Gets the time-zone ID.
     * 
     * @return the zone ID, not null
     */
    @Override
    public ZoneId getZone() {
        return zone;
    }

    /**
     * Returns a copy of this object with the earlier offset at a gap.
     * 
     * @return an {@code IFCZonedDateTime} based on this date-time with the earlier offset, not null
     */
    @Override
    public IFCZonedDateTime withEarlierOffsetAtOverlap() {
        ZonedDateTime zdt = toZonedDateTime().withEarlierOffsetAtOverlap();
        return of(IFCLocalDateTime.of(IFCDate.from(zdt.toLocalDate()), zdt.toLocalTime()), zdt.getZone());
    }

    /**
     * Returns a copy of this object with the later offset at a gap.
     * 
     * @return an {@code IFCZonedDateTime} based on this date-time with the later offset, not null
     */
    @Override
    public IFCZonedDateTime withLaterOffsetAtOverlap() {
        ZonedDateTime zdt = toZonedDateTime().withLaterOffsetAtOverlap();
        return of(IFCLocalDateTime.of(IFCDate.from(zdt.toLocalDate()), zdt.toLocalTime()), zdt.getZone());
    }

    /**
     * Returns a copy of this object with a different time-zone, retaining the local date-time if possible.
     * 
     * @param zone the time-zone to change to, not null
     * @return an {@code IFCZonedDateTime} based on this date-time with the requested zone, not null
     */
    @Override
    public IFCZonedDateTime withZoneSameLocal(ZoneId zone) {
        return of(dateTime, zone);
    }

    /**
     * Returns a copy of this object with a different time-zone, retaining the instant.
     * 
     * @param zone the time-zone to change to, not null
     * @return an {@code IFCZonedDateTime} based on this date-time with the requested zone, not null
     */
    @Override
    public IFCZonedDateTime withZoneSameInstant(ZoneId zone) {
        ZonedDateTime zdt = toZonedDateTime().withZoneSameInstant(zone);
        return of(IFCLocalDateTime.of(IFCDate.from(zdt.toLocalDate()), zdt.toLocalTime()), zdt.getZone());
    }

    /**
     * Checks if the specified field is supported.
     * 
     * @param field the field to check, null returns false
     * @return true if the field is supported
     */
    @Override
    public boolean isSupported(TemporalField field) {
        return dateTime.isSupported(field);
    }

    /**
     * Gets the value of the specified field as a {@code long}.
     * 
     * @param field the field to get, not null
     * @return the value for the field
     */
    @Override
    public long getLong(TemporalField field) {
        return dateTime.getLong(field);
    }

    /**
     * Converts this date-time to an {@code Instant}.
     * 
     * @return an {@code Instant} representing the same point on the time-line, not null
     */
    @Override
    public Instant toInstant() {
        return dateTime.toLocalDateTime().toInstant(offset);
    }

    /**
     * Converts this date-time to the number of seconds from the epoch of 1970-01-01T00:00:00Z.
     * 
     * @return the number of seconds from the epoch of 1970-01-01T00:00:00Z
     */
    @Override
    public long toEpochSecond() {
        return dateTime.toLocalDateTime().toEpochSecond(offset);
    }

    /**
     * Adjusts the specified temporal object to have the same instant as this object.
     * 
     * @param temporal the target object to be adjusted, not null
     * @return the adjusted object, not null
     */
    @Override
    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.INSTANT_SECONDS, toInstant().getEpochSecond())
                .with(ChronoField.NANO_OF_SECOND, toInstant().getNano());
    }

    /**
     * Checks if this zoned date-time is equal to another.
     * 
     * @param o the object to check, null returns false
     * @return true if this is equal to the other zoned date-time
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IFCZonedDateTime that = (IFCZonedDateTime) o;
        return Objects.equals(dateTime, that.dateTime) && Objects.equals(zone, that.zone) && Objects.equals(offset, that.offset);
    }

    /**
     * A hash code for this zoned date-time.
     * 
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(dateTime, zone, offset);
    }

    /**
     * Outputs this zoned date-time as a {@code String}, such as {@code 2023-JANUARY-01T10:30[UTC]}.
     * 
     * @return a string representation of this zoned date-time, not null
     */
    @Override
    public String toString() {
        return dateTime + "[" + zone.getId() + "]";
    }

    /**
     * Converts this date-time to a standard {@link ZonedDateTime}.
     * 
     * @return the equivalent ISO-8601 zoned date-time, not null
     */
    private ZonedDateTime toZonedDateTime() {
        return ZonedDateTime.of(dateTime.toLocalDateTime(), zone);
    }

    /**
     * Returns an object of the same type as this object with the specified field altered.
     * 
     * @param field the field to set in the result, not null
     * @param newValue the new value of the field in the result
     * @return an {@code IFCZonedDateTime} based on this date-time with the specified field set, not null
     */
    @Override
    public IFCZonedDateTime with(TemporalField field, long newValue) {
        if (field.isDateBased()) {
            return of(IFCLocalDateTime.of(dateTime.toLocalDate().with(field, newValue), dateTime.toLocalTime()), zone);
        } else {
            return of(IFCLocalDateTime.of(dateTime.toLocalDate(), dateTime.toLocalTime().with(field, newValue)), zone);
        }
    }

    /**
     * Returns an object of the same type as this object with the specified period added.
     * 
     * @param amountToAdd the amount of the unit to add to the result, may be negative
     * @param unit the unit of the amount to add, not null
     * @return an {@code IFCZonedDateTime} based on this date-time with the specified period added, not null
     */
    @Override
    public IFCZonedDateTime plus(long amountToAdd, TemporalUnit unit) {
        return of(dateTime.plus(amountToAdd, unit), zone);
    }

    /**
     * Calculates the amount of time until another temporal object in terms of a single unit.
     * 
     * @param endExclusive the end date-time, exclusive, not null
     * @param unit the unit to measure the amount in, not null
     * @return the amount of time between this date-time and the end date-time
     */
    @Override
    public long until(Temporal endExclusive, TemporalUnit unit) {
        return dateTime.until(endExclusive, unit);
    }
}
