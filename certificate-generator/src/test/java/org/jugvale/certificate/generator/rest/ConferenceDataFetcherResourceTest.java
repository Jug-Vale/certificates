package org.jugvale.certificate.generator.rest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.fetcher.impl.ConfigurationDataFetcher;
import org.jugvale.certificate.generator.fetcher.impl.csv.CSVConferenceDataFetcher;
import org.jugvale.certificate.generator.model.Attendee;
import org.jugvale.certificate.generator.model.Conference;
import org.jugvale.certificate.generator.model.Registration;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * CertificateModelResourceTest
 */
@QuarkusTest
public class ConferenceDataFetcherResourceTest {

    private static final String DATA_FETCHERS_URI = "/conference-data-fetchers";
    private static final String DATA_FETCHERS_URI_PARAM = DATA_FETCHERS_URI + "/{name}";
    
    @Test
    public void testConferenceDataFetcherResource() {
        Map<?, ?> fetchers = get(DATA_FETCHERS_URI).then()
                                             .statusCode(200)
                                             .extract()
                                             .as(Map.class);
        
        assertEquals(ConfigurationDataFetcher.DESCRIPTION, fetchers.get(ConfigurationDataFetcher.NAME));
        assertEquals(CSVConferenceDataFetcher.DESCRIPTION, fetchers.get(CSVConferenceDataFetcher.NAME));
        post(DATA_FETCHERS_URI_PARAM, "DO_NOT_EXIST").then().statusCode(404);

        ConferenceData data = createConferenceData();
        
        List<Conference> conferences = data.getConferences();
        List<Attendee> attendees = data.getAttendees();
        List<Registration> registrations = data.getRegistrations();
        
        assertEquals(1, conferences.size());
        assertEquals(1, attendees.size());
        assertEquals(1, registrations.size());
        
        assertEquals(ConfigurationDataFetcher.DEFAULT_CONFERENCE_NAME, conferences.get(0).name);
        assertEquals(ConfigurationDataFetcher.DEFAULT_ATTENDEE_NAME, attendees.get(0).name);
        assertEquals(ConfigurationDataFetcher.DEFAULT_ATTENDEE_EMAIL, attendees.get(0).email);
        
        
        delete(DATA_FETCHERS_URI).then().statusCode(204);
    }

    private ConferenceData createConferenceData() {
        return post(DATA_FETCHERS_URI_PARAM, ConfigurationDataFetcher.NAME).then()
                                                                           .statusCode(200)
                                                                           .extract()
                                                                           .as(MutableConferenceData.class);
    }
    
}