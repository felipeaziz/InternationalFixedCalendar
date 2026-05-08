package org.fixedcalendar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.temporal.ChronoUnit;
import static org.junit.jupiter.api.Assertions.*;

class IFCPeriodTest {

    @Test
    @DisplayName("Should create and read values from an IFCPeriod")
    void testCreation() {
        IFCPeriod period = new IFCPeriod(1, 2, 3);
        assertEquals(1, period.get(ChronoUnit.YEARS));
        assertEquals(2, period.get(ChronoUnit.MONTHS));
        assertEquals(3, period.get(ChronoUnit.DAYS));
        assertEquals(IFCChronology.INSTANCE, period.getChronology());
    }

    @Test
    @DisplayName("Should perform basic mathematical operations")
    void testMath() {
        IFCPeriod p1 = new IFCPeriod(1, 1, 1);
        IFCPeriod p2 = new IFCPeriod(0, 2, 5);
        
        assertEquals(new IFCPeriod(1, 3, 6), p1.plus(p2));
        assertEquals(new IFCPeriod(1, -1, -4), p1.minus(p2));
        assertEquals(new IFCPeriod(2, 2, 2), p1.multipliedBy(2));
        assertEquals(new IFCPeriod(-1, -1, -1), p1.negated());
    }

    @Test
    @DisplayName("Should verify period state")
    void testState() {
        assertTrue(new IFCPeriod(0, 0, 0).isZero());
        assertFalse(new IFCPeriod(1, 0, 0).isZero());
        assertTrue(new IFCPeriod(0, -1, 0).isNegative());
        assertFalse(new IFCPeriod(1, 2, 3).isNegative());
    }

    @Test
    @DisplayName("Should add and subtract period from a date")
    void testAddToSubtractFrom() {
        IFCDate start = IFCDate.of(2023, IFCMonth.JANUARY, 1);
        IFCPeriod period = new IFCPeriod(0, 1, 5); // 1 month (28 days) + 5 days = 33 days
        
        IFCDate added = (IFCDate) period.addTo(start);
        assertEquals(IFCMonth.FEBRUARY, added.getMonth());
        assertEquals(6, added.getDayOfMonth());

        IFCDate subtracted = (IFCDate) period.subtractFrom(added);
        assertEquals(start, subtracted);
    }

    @Test
    @DisplayName("Should normalize months and days correctly")
    void testNormalized() {
        // 30 days should become 1 month and 2 days
        IFCPeriod p1 = new IFCPeriod(0, 0, 30);
        assertEquals(new IFCPeriod(0, 1, 2), p1.normalized());

        // 14 months should become 1 year and 1 month
        IFCPeriod p2 = new IFCPeriod(0, 14, 0);
        assertEquals(new IFCPeriod(1, 1, 0), p2.normalized());

        // Complex combination
        IFCPeriod p3 = new IFCPeriod(1, 15, 30); // 1y + 1y 2m + 1m 2d = 2y, 3m, 2d
        assertEquals(new IFCPeriod(2, 3, 2), p3.normalized());
    }

    @Test
    @DisplayName("Should convert to ISO-8601 string format")
    void testToString() {
        assertEquals("P1Y2M3D", new IFCPeriod(1, 2, 3).toString());
    }

    @Test
    @DisplayName("Should ensure equality and hashcode consistency")
    void testEqualsHashCode() {
        IFCPeriod p1 = new IFCPeriod(1, 1, 1);
        IFCPeriod p2 = new IFCPeriod(1, 1, 1);
        IFCPeriod p3 = new IFCPeriod(1, 1, 2);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
    }
}
