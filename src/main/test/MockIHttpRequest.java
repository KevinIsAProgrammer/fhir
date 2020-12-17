import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

class MockIHttpRequest implements IHttpRequest {
    String uri;

    @Override
    public String getUri() {
        return uri;
    }

    MockIHttpRequest(String uri) {
        this.uri = uri;
    }

    // the rest of these shouldn't be called.
    @Override
    public void addHeader(String s, String s1) {
        fail(); // shouldn't be called
    }

    @Override
    public IHttpResponse execute() throws IOException {
        fail(); // shouldn't be called
        return null;
    }

    @Override
    public Map<String, List<String>> getAllHeaders() {
        fail(); // shouldn't be called
        return null;
    }

    @Override
    public String getRequestBodyFromStream() throws IOException {
        fail(); // unused, shouldn't be called
        return null;
    }


    @Override
    public String getHttpVerbName() {
        fail(); // unused, shouldn't be called.
        return null;
    }

    @Override
    public void removeHeaders(String s) {
        fail(); // unused, shouldn't be called
    }
}
