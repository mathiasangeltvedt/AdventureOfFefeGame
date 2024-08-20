package inf112.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestUtilityMethod {

    // Tests the findDigitVal method to ensure it returns the value of a specific
    // digit in a number
    @Test
    void testFindDigitValue() {
        assertEquals(2, UtilityMethods.findDigitVal(123, 1));
        assertEquals(9, UtilityMethods.findDigitVal(923459, 5));

    }

    // Tests the degreesToRadians method to ensure it converts degrees to radians
    // correctly
    @Test
    void degreesToRadiansSanityTest() {
        assertEquals(0, UtilityMethods.degreesToRadians(0));
        assertTrue(Math.abs(UtilityMethods.degreesToRadians(180) - Math.PI) < 1e-5);
        assertTrue(Math.abs(UtilityMethods.degreesToRadians(180) - Math.PI) < 1e-5);
    }

}
