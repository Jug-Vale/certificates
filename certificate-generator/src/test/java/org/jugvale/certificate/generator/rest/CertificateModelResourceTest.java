package org.jugvale.certificate.generator.rest;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.json.bind.JsonbBuilder;

import org.jugvale.certificate.generator.model.CertificateModel;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * CertificateModelResourceTest
 */
@QuarkusTest
public class CertificateModelResourceTest {

    private static final String CERTIFICATE_MODEL_URI = "/certificate-model";
    private static final String CERTIFICATE_MODEL_URI_PARAM = CERTIFICATE_MODEL_URI + "/{name}";
    
    @Test
    public void testCertificateModelResource() throws IOException {
        String svgFile = CertificateModelResourceTest.class.getResource("/svg/simple.svg").getFile();
        CertificateModel model = new CertificateModel();
        model.attendeeNameField = "attendeeName";
        model.certificateKeyField = "certificateKey";
        model.content = new String(Files.readAllBytes(Paths.get(svgFile)), StandardCharsets.UTF_8);
        
        get(CERTIFICATE_MODEL_URI).then()
                                  .statusCode(200)
                                  .body(is("[]"));
        
        String modelStr = JsonbBuilder.create().toJson(model);
        saveCertificateModel(modelStr);
        CertificateModel[] certificates = get(CERTIFICATE_MODEL_URI).then()
                                                                    .statusCode(200)
                                                                    .extract()
                                                                    .as(CertificateModel[].class);
        model = certificates[0];
         
        assertEquals(1, certificates.length);
        assertEquals(model.name, model.name);
        assertEquals(model.content, model.content);
        assertEquals(model.attendeeNameField, model.attendeeNameField);
        assertEquals(model.certificateKeyField, model.certificateKeyField);
          
        delete(CERTIFICATE_MODEL_URI_PARAM, model.id).then()
                                                     .statusCode(204)
                                                     .body(is(""));
        get(CERTIFICATE_MODEL_URI).then()
                                  .statusCode(200)
                                  .body(is("[]"));
    }
    
    private void saveCertificateModel(String modelStr) {
        given().when().contentType("application/json").body(modelStr)
                                                      .put(CERTIFICATE_MODEL_URI)
                                                      .then()
                                                      .statusCode(201);
    }
    
}