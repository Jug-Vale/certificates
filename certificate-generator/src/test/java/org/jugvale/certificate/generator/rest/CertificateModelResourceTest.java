package org.jugvale.certificate.generator.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import javax.json.bind.JsonbBuilder;

import org.jugvale.certificate.generator.model.CertificateModel;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * CertificateModelResourceTest
 */
@QuarkusTest
public class CertificateModelResourceTest {

    private static final String BASE_URI = "/certificate-model";
    String modelStr = getCertificateJson();

    @Test
    public void testResource() {
        given().when()
               .get(BASE_URI)
               .then()
               .statusCode(200)
               .body(is("[]"));
        given().when()
               .contentType("application/json")
               .body(modelStr)
               .put(BASE_URI)
               .then()
               .statusCode(201)
               .body(is("1"));
        given().when()
               .delete(BASE_URI + "/1")
               .then()
               .statusCode(204)
               .body(is(""));
        given().when()
               .get(BASE_URI)
               .then()
               .statusCode(200)
               .body(is("[]"));
    }

    private String getCertificateJson() {
        CertificateModel model = new CertificateModel();
        model.content = "test";
        model.attendeeNameFieldId = "field";
        String modelStr = JsonbBuilder.create().toJson(model);
        return modelStr;
    }
}