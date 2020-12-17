import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.util.StopWatch;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

class MockIHttpResponse implements IHttpResponse {
    long time;
    public MockIHttpResponse(long t) {
        time = t;
    }
    @Override
    public StopWatch getRequestStopWatch() {
        return new MockStopWatch(time);
    }

    // if any of these methods are called without us expecting them to be, our test should fail.
    public void bufferEntity() throws IOException {
        fail();
    }

    public void close() {
        fail();
    }

    public Reader createReader() throws IOException {
        fail();
        return null;
    }

    public Map<String, List<String>> getAllHeaders() {
        fail();
        return null;
    }

    public List<String> getHeaders(String s) {
        fail();
        return null;
    }

    public String getMimeType() {
        fail();
        return null;
    }

    public Object getResponse() {
        fail();
        return null;
    }

    public int getStatus() {
        fail();
        return 0;
    }

    public String getStatusInfo() {
        fail();
        return null;
    }

    public InputStream readEntity() throws IOException {
        fail();
        return null;
    }
}
