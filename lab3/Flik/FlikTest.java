import static org.junit.Assert.*;
import org.junit.Test;

public class FlikTest {
    @Test
    public void test128() {
        int i = 128;
        assertTrue(Flik.isSameNumber(i, i));
    }
}
