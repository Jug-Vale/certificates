package org.jugvale.certificate.generator.rest;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.json.bind.JsonbBuilder;

import org.apache.commons.io.IOUtils;
import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.fetcher.impl.ConfigurationDataFetcher;
import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateContent;
import org.jugvale.certificate.generator.model.CertificateModel;
import org.jugvale.certificate.generator.model.Registration;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

/**
 * CertificateResourceTest
 */
@QuarkusTest
public class CertificateResourceTest {

    private static final String CERTIFICATE_URI = "/certificate";
    private static final String CERTIFICATE_URI_PARAM = CERTIFICATE_URI + "/{id}";
    private static final String CERTIFICATE_CONTENT_URI = CERTIFICATE_URI_PARAM + "/content";
    private static final String CERTIFICATE_GENERATE = CERTIFICATE_URI + "/model/{modelId}/registration/{registrationId}";
    private static final String CERTIFICATE_MODEL_URI = "/certificate-model";
    private static final String DATA_FETCHERS_URI = "/conference-data-fetchers";
    private static final String DATA_FETCHERS_URI_PARAM = DATA_FETCHERS_URI + "/{name}";

    @Test
    public void testCertificateResource() throws Exception {
        createConferenceData();
        String svgFile = CertificateResourceTest.class.getResource("/svg/simple.svg").getFile();
        CertificateModel model = new CertificateModel();
        model.attendeeNameField = "attendeeName";
        model.certificateKeyField = "certificateKey";
        model.content = new String(Files.readAllBytes(Paths.get(svgFile)), StandardCharsets.UTF_8);
        String modelStr = JsonbBuilder.create().toJson(model);
        saveCertificateModel(modelStr);
        model = get(CERTIFICATE_MODEL_URI).then()
                                          .extract()
                                          .as(CertificateModel[].class)[0];
        
        Registration registration = createConferenceData().getRegistrations().get(0);
        
        post(CERTIFICATE_GENERATE, model.id, 123456l).then().statusCode(412);
        post(CERTIFICATE_GENERATE, 123456l, registration.id).then().statusCode(412);
        Certificate certificate = post(CERTIFICATE_GENERATE, model.id, registration.id).then()
                                                                                       .statusCode(200)
                                                                                       .extract()
                                                                                       .as(Certificate.class);
        post(CERTIFICATE_GENERATE, model.id, registration.id).then().statusCode(409);
        
        assertEquals(registration.id, certificate.registration.id);
        assertEquals(model.id, certificate.certificateModel.id);
        assertNotNull(certificate.generationKey);
        
        CertificateContent content = given().accept(ContentType.JSON)
                                            .get(CERTIFICATE_CONTENT_URI, certificate.id)
                                            .then()
                                            .extract()
                                            .as(CertificateContent.class);
        
        assertEquals(certificate.id, content.certificate.id);

        InputStream contentBinByIdIS = given().accept("application/pdf")
                                          .get(CERTIFICATE_CONTENT_URI, certificate.id)
                                          .then()
                                          .statusCode(200)
                                          .extract()
                                          .asInputStream();
        
        byte[] contentBinById = IOUtils.toByteArray(contentBinByIdIS);
        
        assertArrayEquals(contentBinById, content.contentBin);
        
        InputStream contentBinByKeyIS = given().accept("application/pdf")
                                              .get(CERTIFICATE_CONTENT_URI, certificate.id)
                                              .then()
                                              .statusCode(200)
                                              .extract()
                                              .asInputStream();

        byte[] contentBinByKey = IOUtils.toByteArray(contentBinByKeyIS);
        
        assertArrayEquals(contentBinByKey, content.contentBin);
        
        get(CERTIFICATE_CONTENT_URI, "not_existing_key").then().statusCode(404);
        
        delete(CERTIFICATE_URI_PARAM, certificate.id).then().statusCode(204);
    }
    
    private ConferenceData createConferenceData() {
        return post(DATA_FETCHERS_URI_PARAM, ConfigurationDataFetcher.NAME).then()
                                                                           .statusCode(200)
                                                                           .extract()
                                                                           .as(MutableConferenceData.class);
    }
    
    private void saveCertificateModel(String modelStr) {
        given().when().contentType("application/json")
                      .body(modelStr)
                      .put(CERTIFICATE_MODEL_URI)
                      .then()
                      .statusCode(201);
    }
    
}