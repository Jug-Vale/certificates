package org.jugvale.certificate.generator.rest;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import javax.json.bind.JsonbBuilder;

import org.jugvale.certificate.generator.fetcher.impl.ConfigurationDataFetcher;
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

    @Test
    public void testCertificateModelResource() {
        CertificateModel certificateModel = new CertificateModel();
        CertificateModel[] certificates;
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
        Map fetchers = get(DATA_FETCHERS_URI).then()
                                             .statusCode(200)
                                             .extract()
                                             .as(Map.class);
        assertEquals(ConfigurationDataFetcher.DESCRIPTION, fetchers.get(ConfigurationDataFetcher.NAME));
        assertEquals(ConfigurationDataFetcher.DESCRIPTION, fetchers.get(ConfigurationDataFetcher.NAME));
        post(DATA_FETCHERS_URI + "/NON_EXISTING_FETCHER").then().statusCode(404);

        post(DATA_FETCHERS_URI + "/" + ConfigurationDataFetcher.NAME).then().statusCode(200);
    }
}