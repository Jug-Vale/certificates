package org.jugvale.certificate.generator.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

import javax.json.bind.JsonbBuilder;

import org.jugvale.certificate.generator.fetcher.impl.ConfigurationDataFetcher;
import org.jugvale.certificate.generator.fetcher.impl.csv.CSVConferenceDataFetcher;
import org.jugvale.certificate.generator.model.CertificateModel;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * CertificateModelResourceTest
 */
@QuarkusTest
public class RESTResourcesTest {

    private static final String CERTIFICATE_MODEL_URI = "/certificate-model";
    private static final String DATA_FETCHERS_URI = "/conference-data-fetchers";
    
    String modelStr = getCertificateJson();

    @Test
    public void testCertificateModelResource() {
        given().when()
               .get(CERTIFICATE_MODEL_URI)
               .then()
               .statusCode(200)
               .body(is("[]"));
        given().when()
               .contentType("application/json")
               .body(modelStr)
               .put(CERTIFICATE_MODEL_URI)
               .then()
               .statusCode(201)
               .body(is("1"));
        given().when()
               .delete(CERTIFICATE_MODEL_URI + "/1")
               .then()
               .statusCode(204)
               .body(is(""));
        given().when()
               .get(CERTIFICATE_MODEL_URI)
               .then()
               .statusCode(200)
               .body(is("[]"));
    }
    
    @Test
    public void testConferenceDataFetcherResource() {
        given().when()
               .get(DATA_FETCHERS_URI)
               .then()
               .statusCode(200)
               .body(allOf(
                       containsString(CSVConferenceDataFetcher.DESCRIPTION),
                       containsString(ConfigurationDataFetcher.DESCRIPTION)
              ));
    }

    private String getCertificateJson() {
        CertificateModel model = new CertificateModel();
        model.content = "test";
        model.attendeeNameField = "field";
        String modelStr = JsonbBuilder.create().toJson(model);
        return modelStr;
    }
}