import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SCellTest {

    @Test
    public void testGetOrder() {
        SCell cell = new SCell("test");
        assertEquals(0, cell.getOrder());
    }

    @Test
    public void testToString() {
        SCell cell = new SCell("test");
        assertEquals("test", cell.toString());
    }

    @Test
    public void testSetData() {
        SCell cell = new SCell("initial");
        cell.setData("updated");
        assertEquals("updated", cell.getData());
    }

    @Test
    public void testGetData() {
        SCell cell = new SCell("data");
        assertEquals("data", cell.getData());
    }

    @Test
    public void testGetType() {
        SCell cell = new SCell("type");
        cell.setType(1);
        assertEquals(1, cell.getType());
    }

    @Test
    public void testSetType() {
        SCell cell = new SCell("type");
        cell.setType(2);
        assertEquals(2, cell.getType());
    }

    @Test
    public void testSetOrder() {
        SCell cell = new SCell("order");
        cell.setOrder(1);
        assertEquals(0, cell.getOrder()); // תמיד מחזיר 0
    }

}
