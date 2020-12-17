import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;

public class ResponseTimeInterceptor implements IClientInterceptor {
    private boolean ignoreRequest;
    private String urlPrefix;
    private AverageCalculator averageCalculator;

    public ResponseTimeInterceptor(String urlPrefix) {
        this.averageCalculator = new AverageCalculator();
        this.urlPrefix = urlPrefix;
    }

    public ResponseTimeInterceptor(String urlPrefix, AverageCalculator c) {
        this.averageCalculator = c;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void interceptRequest(IHttpRequest iHttpRequest) {
        // only include requests that are for the actual urls we want to time;
        // ignore requests to other urls, such as "/Meta"
        ignoreRequest = !iHttpRequest.getUri().startsWith(urlPrefix);
    }

    boolean willIgnoreRequest() {
        return ignoreRequest;
    }

    @Override
    public void interceptResponse(IHttpResponse iHttpResponse) {
        if (!ignoreRequest) {
            averageCalculator.addData(iHttpResponse.getRequestStopWatch().getMillis());
        }
    }

    double getAverageResponseTime() {
        return averageCalculator.getAverage();
    }

    public void reset() {
        averageCalculator.reset();
    }
}

