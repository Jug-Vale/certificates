package org.jugvale.certificate.generator.rest;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import javax.json.bind.JsonbBuilder;

import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.fetcher.impl.ConfigurationDataFetcher;
import org.jugvale.certificate.generator.fetcher.impl.csv.CSVConferenceDataFetcher;
import org.jugvale.certificate.generator.model.Attendee;
import org.jugvale.certificate.generator.model.CertificateModel;
import org.jugvale.certificate.generator.model.Conference;
import org.jugvale.certificate.generator.model.Registration;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * CertificateModelResourceTest
 */
@QuarkusTest
public class RESTResourcesTest {

    private static final String CERTIFICATE_MODEL_URI = "/certificate-model";
    private static final String DATA_FETCHERS_URI = "/conference-data-fetchers";

    @Test
    public void testCertificateModelResource() {
        CertificateModel certificateModel = new CertificateModel();
        CertificateModel[] certificates;
        certificateModel.name = "Test Certificate";
        certificateModel.content = "certificate content";
        certificateModel.attendeeNameField = "attendeeNamefield";
        certificateModel.certificateKeyField = "certificateKey";
        String modelStr = JsonbBuilder.create().toJson(certificateModel);
        
        get(CERTIFICATE_MODEL_URI).then()
                                  .statusCode(200)
                                  .body(is("[]"));
        
        given().when().contentType("application/json").body(modelStr).put(CERTIFICATE_MODEL_URI)
                                                                     .then()
                                                                     .statusCode(201);
         certificates = get(CERTIFICATE_MODEL_URI).then()
                                                  .statusCode(200)
                                                  .extract()
                                                  .as(CertificateModel[].class);
         
         assertEquals(1, certificates.length);
         assertEquals(certificateModel.name, certificates[0].name);
         assertEquals(certificateModel.content, certificates[0].content);
         assertEquals(certificateModel.attendeeNameField, certificates[0].attendeeNameField);
         assertEquals(certificateModel.certificateKeyField, certificates[0].certificateKeyField);
          
          delete(CERTIFICATE_MODEL_URI + "/" + certificates[0].id).then()
                                                                  .statusCode(204)
                                                                  .body(is(""));
          get(CERTIFICATE_MODEL_URI).then()
                                    .statusCode(200)
                                    .body(is("[]"));
    }

    @Test
    public void testConferenceDataFetcherResource() {
        Map<?, ?> fetchers = get(DATA_FETCHERS_URI).then()
                                             .statusCode(200)
                                             .extract()
                                             .as(Map.class);
        
        assertEquals(ConfigurationDataFetcher.DESCRIPTION, fetchers.get(ConfigurationDataFetcher.NAME));
        assertEquals(CSVConferenceDataFetcher.DESCRIPTION, fetchers.get(CSVConferenceDataFetcher.NAME));
        post(DATA_FETCHERS_URI + "/NON_EXISTING_FETCHER").then().statusCode(404);

        ConferenceData data = post(DATA_FETCHERS_URI + "/" + ConfigurationDataFetcher.NAME).then()
                                                                                           .statusCode(200)
                                                                                           .extract()
                                                                                           .as(MutableConferenceData.class);
        
        List<Conference> conferences = data.getConferences();
        List<Attendee> attendees = data.getAttendees();
        List<Registration> registrations = data.getRegistrations();
        
        assertEquals(1, conferences.size());
        assertEquals(1, attendees.size());
        assertEquals(1, registrations.size());
        
        assertEquals(ConfigurationDataFetcher.DEFAULT_CONFERENCE_NAME, conferences.get(0).name);
        assertEquals(ConfigurationDataFetcher.DEFAULT_ATTENDEE_NAME, attendees.get(0).name);
        assertEquals(ConfigurationDataFetcher.DEFAULT_ATTENDEE_EMAIL, attendees.get(0).email);
        
    }
    
    
    public void testCertificateResource() {
        // TODO:
    }
    
    public static class MutableConferenceData extends ConferenceData {
        
        public MutableConferenceData() {}
        
    }
}