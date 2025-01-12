import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Ex2SheetTest {

    private Ex2Sheet sheet;

    @BeforeEach
    public void setUp() {
        sheet = new Ex2Sheet(5, 5);
    }

    @Test
    public void testGetCell() {
        sheet.set(1, 1, "10");
        Cell cell = sheet.get(1, 1);
        assertNotNull(cell, "Not Found");
        assertEquals("10", cell.getData(), "Error");
    }


    @Test
    public void testIsInValidCoordinates() {
        // בדיקת גבולות
        assertTrue(sheet.isIn(1, 1), "Error");
        assertFalse(sheet.isIn(5, 5), "Error");
    }



    @Test
    public void testCycleDetection() {
        sheet.set(1, 1, "=A0+1");
        sheet.set(0, 0, "=B1+1");

        String result = sheet.eval(0, 0);
        assertEquals(Ex2Utils.ERR_CYCLE, result, "Error");
    }

    @Test
    public void testEvalWithText() {
        sheet.set(1, 1, "Hello");
        String result = sheet.eval(1, 1);
        assertEquals("Hello", result, "Error");
    }

    @Test
    public void testEvalWithInvalidFormula() {
        sheet.set(1, 1, "=A0++");
        String result = sheet.eval(1, 1);
        assertEquals(Ex2Utils.ERR_FORM, result, "הנוסחה לא החזירה שגיאה נכונה");
    }

}
