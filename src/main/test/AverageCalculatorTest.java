import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AverageCalculatorTest {
    @Test
    public void testCreate() {
        AverageCalculator a = new AverageCalculator();
        assertEmpty(a);
    }

    private void assertEmpty(AverageCalculator a) {
        assertEquals(0, a.getNumberOfItems());
        assertEquals(0, a.getTotal());
    }

    @Test
    public void testAdd() {
        AverageCalculator a = new AverageCalculator();
        a.addData(10);
        assertEquals(1, a.getNumberOfItems());
        assertEquals( 10, a.getTotal());

        a.addData(3);
        assertEquals(2, a.getNumberOfItems());
        assertEquals(13, a.getTotal());

        a.addData(2);
        assertEquals(3, a.getNumberOfItems());
        assertEquals(15, a.getTotal());
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
