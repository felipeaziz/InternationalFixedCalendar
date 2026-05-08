package org.fixedcalendar;

import java.time.DateTimeException;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;

/**
 * A period-based amount of time in the International Fixed Calendar system.
 * <p>
 * This class represents an amount of time in terms of years, months and days
 * specifically for the 13-month International Fixed Calendar.
 * <p>
 * This class is immutable and thread-safe.
 */
public final class IFCPeriod implements ChronoPeriod {
    private final int years, months, days;

    /**
     * Creates an instance of {@code IFCPeriod} with years, months and days.
     * 
     * @param years the number of years in the period
     * @param months the number of months in the period
     * @param days the number of days in the period
     */
    public IFCPeriod(int years, int months, int days) {
        this.years = years;
        this.months = months;
        this.days = days;
    }

    /**
     * Gets the value of the requested unit.
     * 
     * @param unit the {@code TemporalUnit} to query, not null
     * @return the value of the unit
     * @throws DateTimeException if the unit is not supported
     */
    @Override
    public long get(TemporalUnit unit) {
        if (unit == ChronoUnit.YEARS) return years;
        if (unit == ChronoUnit.MONTHS) return months;
        if (unit == ChronoUnit.DAYS) return days;
        return 0;
    }

    /**
     * Gets the list of units supported by this period.
     * 
     * @return a list containing {@code YEARS}, {@code MONTHS} and {@code DAYS}, not null
     */
    @Override
    public List<TemporalUnit> getUnits() {
        return List.of(ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS);
    }

    /**
     * Gets the chronology of this period.
     * 
     * @return the {@code IFCChronology} instance, not null
     */
    @Override
    public Chronology getChronology() {
        return IFCChronology.INSTANCE;
    }

    /**
     * Returns a copy of this period with the amounts multiplied by the scalar.
     * 
     * @param scalar the scalar to multiply by
     * @return an {@code IFCPeriod} based on this period with the amounts multiplied by the scalar, not null
     */
    @Override
    public IFCPeriod multipliedBy(int scalar) {
        return new IFCPeriod(years * scalar, months * scalar, days * scalar);
    }

    /**
     * Returns a copy of this period with the amounts negated.
     * 
     * @return an {@code IFCPeriod} based on this period with the amounts negated, not null
     */
    @Override
    public IFCPeriod negated() {
        return new IFCPeriod(-years, -months, -days);
    }

    /**
     * Checks if all units of this period are zero.
     * 
     * @return true if this period is zero-length
     */
    @Override
    public boolean isZero() {
        return years == 0 && months == 0 && days == 0;
    }

    /**
     * Checks if any of the units of this period are negative.
     * 
     * @return true if any unit is less than zero
     */
    @Override
    public boolean isNegative() {
        return years < 0 || months < 0 || days < 0;
    }

    /**
     * Returns a copy of this period with the specified period added.
     * 
     * @param amountToAdd the amount to add, must be an {@code IFCPeriod}, not null
     * @return an {@code IFCPeriod} based on this period with the requested period added, not null
     * @throws DateTimeException if the amount to add is not an {@code IFCPeriod}
     */
    @Override
    public IFCPeriod plus(TemporalAmount amountToAdd) {
        if (amountToAdd instanceof IFCPeriod other) {
            return new IFCPeriod(this.years + other.years, this.months + other.months, this.days + other.days);
        }
        throw new DateTimeException("Only IFCPeriod can be added to IFCPeriod");
    }

    /**
     * Returns a copy of this period with the specified period subtracted.
     * 
     * @param amountToSubtract the amount to subtract, must be an {@code IFCPeriod}, not null
     * @return an {@code IFCPeriod} based on this period with the requested period subtracted, not null
     * @throws DateTimeException if the amount to subtract is not an {@code IFCPeriod}
     */
    @Override
    public IFCPeriod minus(TemporalAmount amountToSubtract) {
        if (amountToSubtract instanceof IFCPeriod other) {
            return new IFCPeriod(this.years - other.years, this.months - other.months, this.days - other.days);
        }
        throw new DateTimeException("Only IFCPeriod can be subtracted from IFCPeriod");
    }

    /**
     * Returns a copy of this period with the amounts of each unit normalized.
     * <p>
     * This normalizes the days and months using the standard IFC lengths:
     * 28 days = 1 month and 13 months = 1 year.
     * 
     * @return a normalized period, not null
     */
    @Override
    public IFCPeriod normalized() {
        long totalMonths = years * 13L + months + days / 28;
        int remainingDays = days % 28;
        int normalizedYears = Math.toIntExact(totalMonths / 13);
        int normalizedMonths = Math.toIntExact(totalMonths % 13);
        return new IFCPeriod(normalizedYears, normalizedMonths, remainingDays);
    }

    /**
     * Adds this period to the specified temporal object.
     * 
     * @param temporal the temporal object to adjust, not null
     * @return an object of the same type with the adjustment made, not null
     */
    @Override
    public Temporal addTo(Temporal temporal) {
        return temporal.plus(years, ChronoUnit.YEARS).plus(months, ChronoUnit.MONTHS).plus(days, ChronoUnit.DAYS);
    }

    /**
     * Subtracts this period from the specified temporal object.
     * 
     * @param temporal the temporal object to adjust, not null
     * @return an object of the same type with the adjustment made, not null
     */
    @Override
    public Temporal subtractFrom(Temporal temporal) {
        return temporal.minus(years, ChronoUnit.YEARS).minus(months, ChronoUnit.MONTHS).minus(days, ChronoUnit.DAYS);
    }

    /**
     * Checks if this period is equal to another period.
     * 
     * @param o the object to check, null returns false
     * @return true if this is equal to the other period
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IFCPeriod ifcPeriod = (IFCPeriod) o;
        return years == ifcPeriod.years && months == ifcPeriod.months && days == ifcPeriod.days;
    }

    /**
     * A hash code for this period.
     * 
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(years, months, days);
    }

    /**
     * Outputs this period as a {@code String}, such as {@code P1Y2M3D}.
     * 
     * @return a string representation of this period, not null
     */
    @Override
    public String toString() {
        return "P" + years + "Y" + months + "M" + days + "D";
    }
}
