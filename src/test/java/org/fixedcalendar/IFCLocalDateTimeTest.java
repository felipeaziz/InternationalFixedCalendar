package org.fixedcalendar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class IFCLocalDateTimeTest {

    private final IFCDate sampleDate = IFCDate.of(2023, IFCMonth.JANUARY, 1);
    private final LocalTime sampleTime = LocalTime.of(10, 30, 0);
    private final IFCLocalDateTime sampleIfcDateTime = IFCLocalDateTime.of(sampleDate, sampleTime);

    @Test
    @DisplayName("Should correctly create IFCLocalDateTime from IFCDate and LocalTime")
    void testOf() {
        assertNotNull(sampleIfcDateTime);
        assertEquals(sampleDate, sampleIfcDateTime.toLocalDate());
        assertEquals(sampleTime, sampleIfcDateTime.toLocalTime());
    }

    @Test
    @DisplayName("Should return the correct chronology")
    void testGetChronology() {
        assertEquals(IFCChronology.INSTANCE, sampleIfcDateTime.getChronology());
    }

    @Test
    @DisplayName("Should correctly add days")
    void testPlusDays() {
        IFCLocalDateTime newDateTime = sampleIfcDateTime.plus(1, ChronoUnit.DAYS);
        assertEquals(IFCDate.of(2023, IFCMonth.JANUARY, 2), newDateTime.toLocalDate());
        assertEquals(sampleTime, newDateTime.toLocalTime());
    }

    @Test
    @DisplayName("Should correctly add hours")
    void testPlusHours() {
        IFCLocalDateTime newDateTime = sampleIfcDateTime.plus(2, ChronoUnit.HOURS);
        assertEquals(sampleDate, newDateTime.toLocalDate());
        assertEquals(LocalTime.of(12, 30, 0), newDateTime.toLocalTime());
    }

    @Test
    @DisplayName("Should correctly subtract days")
    void testMinusDays() {
        IFCLocalDateTime newDateTime = sampleIfcDateTime.minus(1, ChronoUnit.DAYS);
        assertEquals(IFCDate.of(2022, IFCMonth.DECEMBER, 29), newDateTime.toLocalDate());
        assertEquals(sampleTime, newDateTime.toLocalTime());
    }

    @Test
    @DisplayName("Should convert to standard LocalDateTime")
    void testToLocalDateTime() {
        LocalDate isoDate = sampleDate.toLocalDate();
        LocalDateTime expected = LocalDateTime.of(isoDate, sampleTime);
        assertEquals(expected, sampleIfcDateTime.toLocalDateTime());
    }

    @Test
    @DisplayName("Should convert to IFCZonedDateTime")
    void testAtZone() {
        ZoneId zone = ZoneId.of("America/Sao_Paulo");
        IFCZonedDateTime zonedDateTime = (IFCZonedDateTime) sampleIfcDateTime.atZone(zone);
        assertNotNull(zonedDateTime);
        assertEquals(sampleIfcDateTime, zonedDateTime.toLocalDateTime());
        assertEquals(zone, zonedDateTime.getZone());
    }

    @Test
    @DisplayName("Should adjust date field")
    void testWithDateField() {
        IFCLocalDateTime newDateTime = sampleIfcDateTime.with(ChronoField.DAY_OF_MONTH, 5);
        assertEquals(IFCDate.of(2023, IFCMonth.JANUARY, 5), newDateTime.toLocalDate());
        assertEquals(sampleTime, newDateTime.toLocalTime());
    }

    @Test
    @DisplayName("Should adjust time field")
    void testWithTimeField() {
        IFCLocalDateTime newDateTime = sampleIfcDateTime.with(ChronoField.HOUR_OF_DAY, 15);
        assertEquals(sampleDate, newDateTime.toLocalDate());
        assertEquals(LocalTime.of(15, 30, 0), newDateTime.toLocalTime());
    }

    @Test
    @DisplayName("Should verify equality and hashcode")
    void testEqualsAndHashCode() {
        IFCLocalDateTime same = IFCLocalDateTime.of(sampleDate, sampleTime);
        IFCLocalDateTime differentDate = IFCLocalDateTime.of(IFCDate.of(2023, IFCMonth.JANUARY, 2), sampleTime);
        IFCLocalDateTime differentTime = IFCLocalDateTime.of(sampleDate, LocalTime.of(11, 0));

        assertEquals(sampleIfcDateTime, same);
        assertEquals(sampleIfcDateTime.hashCode(), same.hashCode());
        assertNotEquals(sampleIfcDateTime, differentDate);
        assertNotEquals(sampleIfcDateTime, differentTime);
        assertNotEquals(null, sampleIfcDateTime);
    }

    @Test
    @DisplayName("Should return correct string representation")
    void testToString() {
        String expected = "2023-JANUARY-01T10:30";
        assertEquals(expected, sampleIfcDateTime.toString());
    }
}
