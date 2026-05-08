package org.fixedcalendar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class IFCMonthTest {

    @Test
    @DisplayName("Should return the correct month from numeric value")
    void testOf() {
        assertEquals(IFCMonth.JANUARY, IFCMonth.of(1));
        assertEquals(IFCMonth.SOL, IFCMonth.of(7));
        assertEquals(IFCMonth.DECEMBER, IFCMonth.of(13));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 14, -1})
    @DisplayName("Should throw exception for invalid month values")
    void testInvalidMonth(int month) {
        assertThrows(IllegalArgumentException.class, () -> IFCMonth.of(month));
    }

    @Test
    @DisplayName("Should ensure Sol is the seventh month")
    void testSolPosition() {
        assertEquals(7, IFCMonth.SOL.getValue());
        assertEquals("SOL", IFCMonth.SOL.name());
    }
}
