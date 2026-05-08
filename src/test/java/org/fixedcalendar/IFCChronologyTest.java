package org.fixedcalendar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IFCChronologyTest {

    @Test
    @DisplayName("Should return the correct ID and calendar type")
    void testIdAndType() {
        assertEquals("IFC", IFCChronology.INSTANCE.getId());
        assertEquals("ifc", IFCChronology.INSTANCE.getCalendarType());
    }

    @Test
    @DisplayName("Should correctly identify leap years")
    void testIsLeapYear() {
        assertTrue(IFCChronology.INSTANCE.isLeapYear(2024));
        assertFalse(IFCChronology.INSTANCE.isLeapYear(2023));
    }

    @Test
    @DisplayName("Should create date from individual fields")
    void testDate() {
        IFCDate date = IFCChronology.INSTANCE.date(2023, 7, 1);
        assertEquals(2023, date.getYear());
        assertEquals(IFCMonth.SOL, date.getMonth());
        assertEquals(1, date.getDayOfMonth());
    }

    @Test
    @DisplayName("Should create date from epoch day")
    void testDateEpochDay() {
        LocalDate iso = LocalDate.of(1970, 1, 1);
        IFCDate date = IFCChronology.INSTANCE.dateEpochDay(iso.toEpochDay());
        assertEquals(1970, date.getYear());
        assertEquals(IFCMonth.JANUARY, date.getMonth());
        assertEquals(1, date.getDayOfMonth());
    }

    @Test
    @DisplayName("Should create date from day of year")
    void testDateYearDay() {
        IFCDate date = IFCChronology.INSTANCE.dateYearDay(2023, 169); // 169 in common year is Sol 1
        assertEquals(IFCMonth.SOL, date.getMonth());
        assertEquals(1, date.getDayOfMonth());
    }

    @Test
    @DisplayName("Should obtain current date")
    void testDateNow() {
        IFCDate now = IFCChronology.INSTANCE.dateNow();
        assertNotNull(now);
        
        IFCDate nowZone = IFCChronology.INSTANCE.dateNow(ZoneId.of("UTC"));
        assertNotNull(nowZone);

        IFCDate nowClock = IFCChronology.INSTANCE.dateNow(Clock.systemUTC());
        assertNotNull(nowClock);
    }

    @Test
    @DisplayName("Should return correct field ranges")
    void testRange() {
        ValueRange rangeMonth = IFCChronology.INSTANCE.range(ChronoField.MONTH_OF_YEAR);
        assertEquals(1, rangeMonth.getMinimum());
        assertEquals(13, rangeMonth.getMaximum());

        ValueRange rangeDay = IFCChronology.INSTANCE.range(ChronoField.DAY_OF_MONTH);
        assertEquals(1, rangeDay.getMinimum());
        assertEquals(29, rangeDay.getMaximum());
    }

    @Test
    @DisplayName("Should convert temporal accessor to IFCDate")
    void testDateFromTemporal() {
        LocalDate iso = LocalDate.of(2023, 6, 18);
        IFCDate date = IFCChronology.INSTANCE.date(iso);
        assertEquals(IFCMonth.SOL, date.getMonth());
        assertEquals(1, date.getDayOfMonth());
    }

    @Test
    @DisplayName("Should return eras correctly")
    void testEras() {
        List<Era> eras = IFCChronology.INSTANCE.eras();
        assertFalse(eras.isEmpty());
        assertTrue(eras.contains(IsoEra.CE));
        assertEquals(IsoEra.CE, IFCChronology.INSTANCE.eraOf(1));
    }
}
