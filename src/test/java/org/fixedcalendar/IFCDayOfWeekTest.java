package org.fixedcalendar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class IFCDayOfWeekTest {

    @ParameterizedTest
    @CsvSource({
        "1, SUNDAY",
        "2, MONDAY",
        "7, SATURDAY",
        "8, SUNDAY",
        "28, SATURDAY"
    })
    @DisplayName("Should calculate the perpetual day of week based on day of month")
    void testOfDayOfMonth(int dayOfMonth, IFCDayOfWeek expected) {
        assertEquals(expected, IFCDayOfWeek.ofDayOfMonth(dayOfMonth));
    }

    @Test
    @DisplayName("Special days (29) should be correctly identified as INTERCALARY")
    void testSpecialDays() {
        assertEquals(IFCDayOfWeek.INTERCALARY, IFCDayOfWeek.ofDayOfMonth(29));
    }
}
