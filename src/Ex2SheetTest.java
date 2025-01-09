import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Ex2SheetTest {

    @Test
    public void testConstructorWithDimensions() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        assertEquals(5, sheet.width());
        assertEquals(5, sheet.height());
    }

    @Test
    public void testDefaultConstructor() {
        Ex2Sheet sheet = new Ex2Sheet();
        assertEquals(Ex2Utils.WIDTH, sheet.width());
        assertEquals(Ex2Utils.HEIGHT, sheet.height());
    }


    @Test
    public void testGetCellByCoordinates() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        sheet.set(0, 0, "data");
        assertEquals("data", sheet.get(0, 0).getData());
    }

    @Test
    public void testGetCellByPosition() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        sheet.set(0, 0, "data");
        assertEquals("data", sheet.get("A0").getData());
    }

    @Test
    public void testWidth() {
        Ex2Sheet sheet = new Ex2Sheet(3, 4);
        assertEquals(3, sheet.width());
    }

    @Test
    public void testHeight() {
        Ex2Sheet sheet = new Ex2Sheet(3, 4);
        assertEquals(4, sheet.height());
    }

    @Test
    public void testSet() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        sheet.set(1, 1, "test");
        assertEquals("test", sheet.get(1, 1).getData());
    }

    @Test
    public void testEval() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        sheet.eval();
        assertNotNull(sheet);
    }

    @Test
    public void testIsIn() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        assertTrue(sheet.isIn(1, 1));
        assertFalse(sheet.isIn(3, 1));
    }

    @Test
    public void testDepth() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        int[][] depth = sheet.depth();
        assertEquals(2, depth.length);
        assertEquals(2, depth[0].length);
    }


    @Test
    public void testComputeForm() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        List<PositionFinder> positions = new ArrayList<>();
        String result = sheet.computeForm("=5+5", positions);
        assertEquals("10.0", result);
    }

    @Test
    public void testComputeFormHelper() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        List<PositionFinder> positions = new ArrayList<>();
        String result = sheet.computeFormHelper("5+5", positions);
        assertEquals("10.0", result);
    }

    @Test
    public void testIndOfMainOp() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        int index = sheet.indOfMainOp("5+5");
        assertEquals(1, index);
    }

    @Test
    public void testIsOp() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        assertTrue(sheet.isOp('+'));
        assertFalse(sheet.isOp('a'));
    }

    @Test
    public void testIsOpBetter() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        assertTrue(sheet.isOpBetter(1, 1, 1, 0, 0, 0));
    }

    @Test
    public void testSignType() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        assertEquals(0, sheet.signType('+'));
        assertEquals(-1, sheet.signType('a'));
    }

    @Test
    public void testInterpretPos() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        PositionFinder pos = sheet.interpretPos("A0");
        assertEquals(0, pos.x);
        assertEquals(0, pos.y);
    }

    @Test
    public void testPerformOperation() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        assertEquals(10.0, sheet.performOperation(5, 5, '+'));
    }

    @Test
    public void testParseDouble() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        assertEquals(10.0, sheet.parseDouble("10"));
        assertEquals(-1, sheet.parseDouble("a"));
    }

    @Test
    public void testParseInt() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        assertEquals(10, sheet.parseInt("10"));
        assertEquals(-1, sheet.parseInt("a"));
    }

    @Test
    public void testContainsCoordinate() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        List<PositionFinder> positions = new ArrayList<>();
        positions.add(new PositionFinder(0, 0));
        assertTrue(sheet.containsCoordinate(positions, new PositionFinder(0, 0)));
    }

    @Test
    public void testApplyUnOp() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        assertEquals(10.0, sheet.applyUnOp(10, '+'));
        assertEquals(-10.0, sheet.applyUnOp(10, '-'));
    }

}
