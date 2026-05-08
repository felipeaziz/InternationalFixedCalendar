package org.fixedcalendar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class IFCDateTest {

    @Test
    @DisplayName("Should correctly convert from ISO to IFC for normal dates")
    void testFromIsoNormalDate() {
        LocalDate isoDate = LocalDate.of(2023, 1, 1);
        IFCDate ifcDate = IFCDate.from(isoDate);
        assertEquals(2023, ifcDate.getYear());
        assertEquals(IFCMonth.JANUARY, ifcDate.getMonth());
        assertEquals(1, ifcDate.getDayOfMonth());
    }

    @Test
    @DisplayName("Should correctly identify Year Day (December 29 in IFC)")
    void testYearDay() {
        LocalDate isoDate = LocalDate.of(2023, 12, 31);
        IFCDate ifcDate = IFCDate.from(isoDate);
        assertTrue(ifcDate.isYearDay(), "ISO 12/31 should be Year Day in IFC");
        assertEquals(IFCMonth.DECEMBER, ifcDate.getMonth());
        assertEquals(29, ifcDate.getDayOfMonth());
    }

    @Test
    @DisplayName("Should correctly identify Leap Day (June 29 in IFC)")
    void testLeapDay() {
        LocalDate isoDate = LocalDate.of(2024, 6, 17);
        IFCDate ifcDate = IFCDate.from(isoDate);
        assertTrue(ifcDate.isLeapDay(), "ISO 06/17/2024 should be Leap Day in IFC");
        assertEquals(IFCMonth.JUNE, ifcDate.getMonth());
        assertEquals(29, ifcDate.getDayOfMonth());
    }

    @ParameterizedTest
    @CsvSource({
        "2023-01-01, 2023, JANUARY, 1",
        "2023-02-26, 2023, MARCH, 1",
        "2023-06-18, 2023, SOL, 1",
        "2023-12-30, 2023, DECEMBER, 28"
    })
    @DisplayName("Should correctly convert multiple ISO dates to IFC")
    void testMultipleConversions(String isoStr, int expectedYear, IFCMonth expectedMonth, int expectedDay) {
        IFCDate ifcDate = IFCDate.from(LocalDate.parse(isoStr));
        assertEquals(expectedYear, ifcDate.getYear());
        assertEquals(expectedMonth, ifcDate.getMonth());
        assertEquals(expectedDay, ifcDate.getDayOfMonth());
    }

    @Test
    @DisplayName("Should correctly convert back to ISO (Reversibility)")
    void testReversibility() {
        LocalDate originalIso = LocalDate.of(2023, 7, 15);
        IFCDate ifcDate = IFCDate.from(originalIso);
        assertEquals(originalIso, ifcDate.toLocalDate());
    }

    @Test
    @DisplayName("Arithmetic test: adding 1 month should result in 28 days")
    void testPlusMonth() {
        IFCDate start = IFCDate.of(2023, IFCMonth.JANUARY, 1);
        IFCDate nextMonth = start.plus(1, ChronoUnit.MONTHS);
        assertEquals(IFCMonth.FEBRUARY, nextMonth.getMonth());
        assertEquals(1, nextMonth.getDayOfMonth());
    }

    @ParameterizedTest
    @CsvSource({
        "2023, JANUARY, 1, SUNDAY",
        "2023, JANUARY, 2, MONDAY",
        "2023, JANUARY, 7, SATURDAY",
        "2023, JANUARY, 8, SUNDAY",
        "2023, DECEMBER, 29, INTERCALARY",
        "2024, JUNE, 29, INTERCALARY"
    })
    @DisplayName("Should return the correct day of week")
    void testGetDayOfWeek(int year, IFCMonth month, int day, IFCDayOfWeek expected) {
        IFCDate date = IFCDate.of(year, month, day);
        assertEquals(expected, date.getDayOfWeek());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 30, 31, -1})
    @DisplayName("Should throw exception for invalid days of month")
    void testInvalidDaysOfMonth(int day) {
        assertThrows(DateTimeException.class, () -> IFCDate.of(2023, IFCMonth.JANUARY, day));
    }

    @Test
    @DisplayName("Should throw exception for Leap Day in non-leap year")
    void testInvalidLeapDay() {
        assertThrows(DateTimeException.class, () -> IFCDate.of(2023, IFCMonth.JUNE, 29));
    }
}
