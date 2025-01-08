import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CellEntryTest {

    @Test
    public void testIsValid() {
        CellEntry entry1 = new CellEntry('A', 10);
        assertTrue(entry1.isValid());

        CellEntry entry2 = new CellEntry('Z', 99);
        assertTrue(entry2.isValid());

        CellEntry entry3 = new CellEntry('a', 50);
        assertTrue(entry3.isValid());

        CellEntry entry4 = new CellEntry('B', 100);
        assertFalse(entry4.isValid());

        CellEntry entry5 = new CellEntry('!', 10);
        assertFalse(entry5.isValid());
    }

    @Test
    public void testGetX() {
        CellEntry entry1 = new CellEntry('A', 10);
        assertEquals(0, entry1.getX());

        CellEntry entry2 = new CellEntry('B', 20);
        assertEquals(1, entry2.getX());

        CellEntry entry3 = new CellEntry('Z', 30);
        assertEquals(25, entry3.getX());

        CellEntry entry4 = new CellEntry('a', 40);
        assertEquals(0, entry4.getX());

        CellEntry entry5 = new CellEntry('@', 50);
        assertEquals(Ex2Utils.ERR, entry5.getX());
    }

    @Test
    public void testGetY() {
        CellEntry entry1 = new CellEntry('A', 10);
        assertEquals(10, entry1.getY());

        CellEntry entry2 = new CellEntry('B', 99);
        assertEquals(99, entry2.getY());

        CellEntry entry3 = new CellEntry('C', 0);
        assertEquals(0, entry3.getY());

        CellEntry entry4 = new CellEntry('D', -1);
        assertEquals(Ex2Utils.ERR, entry4.getY());

        CellEntry entry5 = new CellEntry('E', 100);
        assertEquals(Ex2Utils.ERR, entry5.getY());
    }

    @Test
    public void testToString() {
        CellEntry entry1 = new CellEntry('A', 10);
        assertEquals("A10", entry1.toString());

        CellEntry entry2 = new CellEntry('B', 99);
        assertEquals("B99", entry2.toString());

        CellEntry entry3 = new CellEntry('C', 0);
        assertEquals("C0", entry3.toString());
    }
}