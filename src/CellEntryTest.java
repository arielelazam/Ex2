import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CellEntryTest {

    @Test
    public void testIsValid() {
        CellEntry entry1 = new CellEntry('A', 10);
        assertTrue(entry1.isValid(), "Entry with A and 10 should be valid.");

        CellEntry entry2 = new CellEntry('Z', 99);
        assertTrue(entry2.isValid(), "Entry with Z and 99 should be valid.");

        CellEntry entry3 = new CellEntry('a', 50);
        assertTrue(entry3.isValid(), "Entry with a and 50 should be valid.");

        CellEntry entry4 = new CellEntry('B', 100);
        assertFalse(entry4.isValid(), "Entry with B and 100 should be invalid.");

        CellEntry entry5 = new CellEntry('!', 10);
        assertFalse(entry5.isValid(), "Entry with ! and 10 should be invalid.");
    }

    @Test
    public void testGetX() {
        CellEntry entry1 = new CellEntry('A', 10);
        assertEquals(0, entry1.getX(), "A should correspond to 0.");

        CellEntry entry2 = new CellEntry('B', 20);
        assertEquals(1, entry2.getX(), "B should correspond to 1.");

        CellEntry entry3 = new CellEntry('Z', 30);
        assertEquals(25, entry3.getX(), "Z should correspond to 25.");

        CellEntry entry4 = new CellEntry('a', 40);
        assertEquals(0, entry4.getX(), "a should correspond to 0.");

        CellEntry entry5 = new CellEntry('@', 50);
        assertEquals(Ex2Utils.ERR, entry5.getX(), "@ should correspond to an error.");
    }

    @Test
    public void testGetY() {
        CellEntry entry1 = new CellEntry('A', 10);
        assertEquals(10, entry1.getY(), "Y value of 10 should be valid.");

        CellEntry entry2 = new CellEntry('B', 99);
        assertEquals(99, entry2.getY(), "Y value of 99 should be valid.");

        CellEntry entry3 = new CellEntry('C', 0);
        assertEquals(0, entry3.getY(), "Y value of 0 should be valid.");

        CellEntry entry4 = new CellEntry('D', -1);
        assertEquals(Ex2Utils.ERR, entry4.getY(), "Y value of -1 should be invalid.");

        CellEntry entry5 = new CellEntry('E', 100);
        assertEquals(Ex2Utils.ERR, entry5.getY(), "Y value of 100 should be invalid.");
    }

    @Test
    public void testToString() {
        CellEntry entry1 = new CellEntry('A', 10);
        assertEquals("A10", entry1.toString(), "toString should return 'A10' for entry A10.");

        CellEntry entry2 = new CellEntry('B', 99);
        assertEquals("B99", entry2.toString(), "toString should return 'B99' for entry B99.");

        CellEntry entry3 = new CellEntry('C', 0);
        assertEquals("C0", entry3.toString(), "toString should return 'C0' for entry C0.");
    }
}
