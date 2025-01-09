import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PositionFinderTest {

    @Test
    public void testConstructor() {
        PositionFinder pos = new PositionFinder(1, 2);
        assertEquals(1, pos.x);
        assertEquals(2, pos.y);
    }

}