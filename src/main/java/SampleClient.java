import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.stream.Collectors;

public class SampleClient {

    public static void main(String[] theArgs) {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));

        // Search for Patient resources
        Bundle response = client
                .search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().value("SMITH"))
                .returnBundle(Bundle.class)
                .execute();

        System.out.println(response.getEntry().stream().
                map(Bundle.BundleEntryComponent::getResource).
                filter(resource -> resource.hasType("Patient")).
                map(resource -> (Patient) resource).
                map(SampleClient::patientDetails).
                sorted()
                .collect(Collectors.joining("\n")));
    }

    static String patientDetails(Patient patient) {
        String first=firstName(patient);
        String last=lastName(patient);
        String birth = birthdate(patient);
        return first + " " + last + " " + birth;
    }
    static String firstName(Patient patient) {
        // TODO: Confirm handling of people with multiple names, no given name, no family name, etc.
        if (!patient.hasName()) {
            return "Nameless";
        }
        return patient.getName().stream().
                filter(name -> name.hasGiven()).
                map(n -> n.getGiven().get(0).toString()).
                findFirst().orElse("NoFirstNameAvailable");
    }
    static String lastName(Patient patient) {
        if (!patient.hasName()) {
            return "Nobody";
        }

        return patient.getName().stream().
                filter(HumanName::hasFamily).
                map(HumanName::getFamily).findFirst().orElse("NoLastNameAvailable");
    }

    static String birthdate(Patient patient) {
        if (!patient.hasBirthDate()) {
            return "Unknown Date of Birth";
        }
        return patient.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
    }
}
