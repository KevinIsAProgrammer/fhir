import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

public class SampleClient {

    public static void main(String[] theArgs) throws IOException {
        String serverBase="http://hapi.fhir.org/baseR4";
        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient cachingClient = fhirContext.newRestfulGenericClient(serverBase);
        IGenericClient nonCachingClient = fhirContext.newRestfulGenericClient(serverBase);

        // Ensure that both clients have the same header construction time by forcing both to construct headers of the
        // same length.
        cachingClient.registerInterceptor(new SimpleRequestHeaderInterceptor("Xache-Control",
                "no-cache")); // fake header, does nothing.
        nonCachingClient.registerInterceptor(new SimpleRequestHeaderInterceptor("Cache-Control",
                "no-cache")); // real header, instructs server not to use cached results.

        // Register an interceptor that will time our searches for the Patient resource
        ResponseTimeInterceptor interceptor = new ResponseTimeInterceptor(serverBase + "/Patient");
        cachingClient.registerInterceptor(interceptor);
        nonCachingClient.registerInterceptor(interceptor);

        List<String> names = getNames("LastNames");

        runSearches(cachingClient, names);
        System.out.printf("Run 1: Average response time: %s ms\n", interceptor.getAverageResponseTime());
        interceptor.reset();

        runSearches(cachingClient, names);
        System.out.printf("Run 2: Average response time: %s ms\n", interceptor.getAverageResponseTime());
        interceptor.reset();

        runSearches(nonCachingClient, names);
        System.out.printf("Run 3: Average response time: %s ms\n", interceptor.getAverageResponseTime());
    }

    static List<String> getNames(String path) throws IOException {
        return Files.readAllLines(Paths.get(path));
    }

    static void runSearches(IGenericClient client, List<String> familyNames) {
        for (String name : familyNames) {
            familySearch(client, name);
        }
    }

    static void familySearch(IGenericClient client, String familyName) {
        client.search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().value(familyName.toUpperCase(Locale.ROOT)))
                .returnBundle(Bundle.class)
                .execute();
    }
}
