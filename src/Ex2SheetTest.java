import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Ex2SheetTest {

    private Ex2Sheet sheet;

    @BeforeEach
    public void setUp() {
        // יצירת אובייקט של Ex2Sheet לפני כל טסט
        sheet = new Ex2Sheet(5, 5);  // גודל של 5x5
    }

    @Test
    public void testGetCell() {
        // הגדרת ערך בתא 1,1
        sheet.set(1, 1, "10");
        // קבלת התא והשוואת הערך
        Cell cell = sheet.get(1, 1);
        assertNotNull(cell, "התו לא נמצא");
        assertEquals("10", cell.getData(), "הערך בתא 1,1 לא תואם");
    }


    @Test
    public void testIsInValidCoordinates() {
        // בדיקת גבולות
        assertTrue(sheet.isIn(1, 1), "תא 1,1 לא נמצא בטווח הגיליון");
        assertFalse(sheet.isIn(5, 5), "תא 5,5 לא אמור להיות בתווך הגיליון");
    }



    @Test
    public void testCycleDetection() {
        // הגדרת נוסחה עם לולאת חישוב בתא
        sheet.set(1, 1, "=A0+1");
        sheet.set(0, 0, "=B1+1");

        String result = sheet.eval(0, 0);
        assertEquals(Ex2Utils.ERR_CYCLE, result, "הנוסחה לא זיהתה לולאת חישוב");
    }

    @Test
    public void testEvalWithText() {
        // הגדרת טקסט בתא
        sheet.set(1, 1, "Hello");
        String result = sheet.eval(1, 1);
        assertEquals("Hello", result, "הערך בתא 1,1 לא טקסט");
    }

    @Test
    public void testEvalWithInvalidFormula() {
        // הגדרת נוסחה לא תקינה
        sheet.set(1, 1, "=A0++");
        String result = sheet.eval(1, 1);
        assertEquals(Ex2Utils.ERR_FORM, result, "הנוסחה לא החזירה שגיאה נכונה");
    }

}
