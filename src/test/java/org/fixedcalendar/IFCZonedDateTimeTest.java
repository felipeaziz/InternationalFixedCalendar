package org.fixedcalendar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class IFCZonedDateTimeTest {

    private final IFCDate sampleDate = IFCDate.of(2023, IFCMonth.JANUARY, 1);
    private final LocalTime sampleTime = LocalTime.of(10, 30, 0);
    private final IFCLocalDateTime sampleIfcDateTime = IFCLocalDateTime.of(sampleDate, sampleTime);
    private final ZoneId sampleZone = ZoneId.of("UTC");
    private final IFCZonedDateTime sampleIfcZonedDateTime = IFCZonedDateTime.of(sampleIfcDateTime, sampleZone);

    @Test
    @DisplayName("Should correctly create IFCZonedDateTime")
    void testOf() {
        assertNotNull(sampleIfcZonedDateTime);
        assertEquals(sampleIfcDateTime, sampleIfcZonedDateTime.toLocalDateTime());
        assertEquals(sampleZone, sampleIfcZonedDateTime.getZone());
        assertEquals(ZoneOffset.UTC, sampleIfcZonedDateTime.getOffset());
    }

    @Test
    @DisplayName("Should return the correct chronology")
    void testGetChronology() {
        assertEquals(IFCChronology.INSTANCE, sampleIfcZonedDateTime.getChronology());
    }

    @Test
    @DisplayName("Should correctly convert to Instant")
    void testToInstant() {
        Instant expected = sampleIfcDateTime.toLocalDateTime().toInstant(ZoneOffset.UTC);
        assertEquals(expected, sampleIfcZonedDateTime.toInstant());
    }

    @Test
    @DisplayName("Should correctly convert to epoch second")
    void testToEpochSecond() {
        long expected = sampleIfcDateTime.toLocalDateTime().toEpochSecond(ZoneOffset.UTC);
        assertEquals(expected, sampleIfcZonedDateTime.toEpochSecond());
    }

    @Test
    @DisplayName("Should change time-zone retaining local date-time")
    void testWithZoneSameLocal() {
        ZoneId newZone = ZoneId.of("America/Sao_Paulo");
        IFCZonedDateTime newZdt = sampleIfcZonedDateTime.withZoneSameLocal(newZone);
        assertEquals(sampleIfcDateTime, newZdt.toLocalDateTime());
        assertEquals(newZone, newZdt.getZone());
    }

    @Test
    @DisplayName("Should change time-zone retaining the instant")
    void testWithZoneSameInstant() {
        ZoneId newZone = ZoneId.of("America/Sao_Paulo");
        IFCZonedDateTime newZdt = sampleIfcZonedDateTime.withZoneSameInstant(newZone);
        assertEquals(sampleIfcZonedDateTime.toInstant(), newZdt.toInstant());
        assertEquals(newZone, newZdt.getZone());
    }

    @Test
    @DisplayName("Should correctly perform arithmetic with time units")
    void testPlusHours() {
        IFCZonedDateTime newZdt = sampleIfcZonedDateTime.plus(5, ChronoUnit.HOURS);
        assertEquals(sampleTime.plusHours(5), newZdt.toLocalDateTime().toLocalTime());
    }

    @Test
    @DisplayName("Should correctly adjust date fields")
    void testWithField() {
        IFCZonedDateTime newZdt = sampleIfcZonedDateTime.with(ChronoField.DAY_OF_MONTH, 15);
        assertEquals(15, newZdt.toLocalDateTime().toLocalDate().getDayOfMonth());
    }

    @Test
    @DisplayName("Should verify equality")
    void testEquals() {
        IFCZonedDateTime same = IFCZonedDateTime.of(sampleIfcDateTime, sampleZone);
        assertEquals(sampleIfcZonedDateTime, same);
        assertEquals(sampleIfcZonedDateTime.hashCode(), same.hashCode());
    }

    @Test
    @DisplayName("Should return correct string representation")
    void testToString() {
        assertTrue(sampleIfcZonedDateTime.toString().contains("2023-JANUARY-01T10:30"));
        assertTrue(sampleIfcZonedDateTime.toString().contains("[UTC]"));
    }
}
