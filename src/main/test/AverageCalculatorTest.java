import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AverageCalculatorTest {
    @Test
    public void testCreate() {
        AverageCalculator a = new AverageCalculator();
        assertEmpty(a);
    }

    private void assertEmpty(AverageCalculator a) {
        assertEquals(0, a.n);
        assertEquals(0, a.total);
    }

    @Test
    public void testAdd() {
        AverageCalculator a = new AverageCalculator();
        a.addData(10);
        assertEquals(1, a.n);
        assertEquals( 10, a.total);

        a.addData(3);
        assertEquals(2, a.n);
        assertEquals(13, a.total);

        a.addData(2);
        assertEquals(3, a.n);
        assertEquals(15, a.total);
    }

    @Test
    public void testAverage() {
        AverageCalculator a = new AverageCalculator();

        a.addData(10);
        a.addData(3);
        a.addData(2);
        assertEquals(5.0, a.getAverage(),0.000000001);

        a.reset();
        assertEquals(-1.0, a.getAverage(),0.00000001);
    }

    @Test
    public void testReset() {
        AverageCalculator a = new AverageCalculator();
        a.addData(10);
        a.reset();
        assertEmpty(a);
    }
}
