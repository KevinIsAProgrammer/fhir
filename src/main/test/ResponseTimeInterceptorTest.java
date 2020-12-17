import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseTimeInterceptorTest {

    @Test
    public void testInterceptRequest() {
        ResponseTimeInterceptor r = new ResponseTimeInterceptor("base/Patient", new MockAverageCalculator());
        assertEquals("base/Patient", r.urlPrefix);

        r.interceptRequest(new MockIHttpRequest("base/Patient?NAME=SMITH"));
        assertFalse(r.ignoreRequest);

        r.interceptRequest(new MockIHttpRequest("base/Meta"));
        assertTrue(r.ignoreRequest);
    }

    // ensure that when our response interceptor is called for a request that we shouldn't ignore,
    // that it correctly calls our average calculator with the correct response time
    // (i.e. the one from the IHttpResponse's stopwatch)
    @Test
    public void testNormalInterceptResponse() {
        long mockResponseTime = 1000;
        MockAverageCalculator t = new MockAverageCalculator();

        ResponseTimeInterceptor r = new ResponseTimeInterceptor("base/Patient", t);
        r.ignoreRequest = false;
        r.interceptResponse(new MockIHttpResponse(mockResponseTime));

        assertTrue(t.addCalled);
        assertEquals(mockResponseTime, t.valueToAdd);
    }

    // Ensure that when our response interceptor is called for a request that it should ignore,
    // that it correctly calls
    @Test
    public void testIgnoredInterceptResponse() {
        long mockResponseTime = 1000;
        MockAverageCalculator t = new MockAverageCalculator();

        ResponseTimeInterceptor r = new ResponseTimeInterceptor("base/Patient", t);
        r.ignoreRequest = true;
        r.interceptResponse(new MockIHttpResponse(mockResponseTime));
        assertFalse(t.addCalled);
    }

    // ensure that we get the average sent by our average calculator
    @Test
    public void getAverageResponseTime() {
        MockAverageCalculator t = new MockAverageCalculator();
        assertFalse(t.averageCalled);
        ResponseTimeInterceptor r = new ResponseTimeInterceptor("abc", t);

        // ensure that we return the same average that our mock average calculator always computes
        assertEquals(100.0, r.getAverageResponseTime(), 0.00000000000001);
        // ensure that we called our mock average calculator
        assertTrue(t.averageCalled);
    }

    // ensure that when we call reset, our calculator resets it's state
    @Test
    public void reset() {
        MockAverageCalculator t = new MockAverageCalculator();
        assertFalse(t.resetCalled);
        ResponseTimeInterceptor r = new ResponseTimeInterceptor("abc", t);
        r.reset();
        assertTrue(t.resetCalled);
    }
}

