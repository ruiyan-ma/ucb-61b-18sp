import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {

    @Test
    public void testEqualChars() {
        OffByN offBy2 = new OffByN(2);
        assertTrue(offBy2.equalChars('a', 'c'));
        assertTrue(offBy2.equalChars('e', 'c'));
        assertFalse(offBy2.equalChars('a', 'd'));

        OffByN offBy5 = new OffByN(5);
        assertTrue(offBy5.equalChars('a', 'f'));
        assertTrue(offBy5.equalChars('f', 'a'));
        assertFalse(offBy5.equalChars('f', 'h'));
    }

}
