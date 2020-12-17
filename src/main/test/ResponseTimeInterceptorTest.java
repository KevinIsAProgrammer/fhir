import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.util.StopWatch;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ResponseTimeInterceptorTest {

    @Test
    public void testInterceptPatientRequest() {
        ResponseTimeInterceptor r = new ResponseTimeInterceptor("base/Patient", new AverageCalculator());

        IHttpRequest req = mock(IHttpRequest.class);
        when(req.getUri()).thenReturn("base/Patient?NAME=SMITH");
        r.interceptRequest(req);
        assertFalse(r.willIgnoreRequest());
    }

    public void testInterceptMetaRequest() {
        ResponseTimeInterceptor r = new ResponseTimeInterceptor("base/Patient", new AverageCalculator());

        IHttpRequest request = mock(IHttpRequest.class);
        when(request.getUri()).thenReturn("base/Meta");
        r.interceptRequest(request);
        assertTrue(r.willIgnoreRequest());
    }

    // ensure that when our response interceptor is called for a request that we shouldn't ignore,
    // that it correctly calls our average calculator with the correct response time
    // (i.e. the one from the IHttpResponse's stopwatch)
    @Test
    public void testNormalInterceptResponse() {
        long mockResponseTime = 1000;
        AverageCalculator c = new AverageCalculator();
        ResponseTimeInterceptor r = new ResponseTimeInterceptor("base/Patient", c);

        StopWatch stopWatch = mock(StopWatch.class);
        when(stopWatch.getMillis()).thenReturn(mockResponseTime);
        IHttpResponse response = mock(IHttpResponse.class);
        when(response.getRequestStopWatch()).thenReturn(stopWatch);

        assertFalse(r.willIgnoreRequest());
        r.interceptResponse(response);
        assertEquals(mockResponseTime, c.getTotal());
        assertEquals(1, c.getNumberOfItems());
    }

    // Ensure that when our response interceptor is called for a request that it should ignore,
    // that it correctly calls
    @Test
    public void testIgnoredInterceptResponse() {
        long mockResponseTime = 1000;
        AverageCalculator c = new AverageCalculator();
        ResponseTimeInterceptor r = new ResponseTimeInterceptor("base/Patient", c);

        // set up a request to ignore our response
        IHttpRequest request = mock(IHttpRequest.class);
        when(request.getUri()).thenReturn("base/Meta");
        r.interceptRequest(request);

        StopWatch stopWatch = mock(StopWatch.class);
        when(stopWatch.getMillis()).thenReturn(mockResponseTime);
        IHttpResponse response = mock(IHttpResponse.class);
        when(response.getRequestStopWatch()).thenReturn(stopWatch);

        assertTrue(r.willIgnoreRequest());
        r.interceptResponse(response);
        assertEquals(0, c.getTotal());
        assertEquals(0, c.getNumberOfItems());

    }

    // ensure that we get the average sent by our average calculator
    @Test
    public void getAverageResponseTime() {
        double mockAverage = 100.0;
        AverageCalculator c = mock(AverageCalculator.class);
        when(c.getAverage()).thenReturn(mockAverage);
        ResponseTimeInterceptor r = new ResponseTimeInterceptor("abc", c);

        // ensure that we return the same average that our mock average calculator always computes
        assertEquals(mockAverage, r.getAverageResponseTime(), 0.00000000000001);
    }

    // ensure that when we call reset, our calculator resets it's state
    @Test
    public void reset() {
        AverageCalculator c = mock(AverageCalculator.class);
        ResponseTimeInterceptor r = new ResponseTimeInterceptor("abc", c);
        r.reset();
        verify(c).reset();
    }
}

