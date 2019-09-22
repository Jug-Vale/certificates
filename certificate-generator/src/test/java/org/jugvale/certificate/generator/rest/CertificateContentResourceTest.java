package org.jugvale.certificate.generator.rest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;

import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.fetcher.impl.ConfigurationDataFetcher;
import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateContent;
import org.jugvale.certificate.generator.model.CertificateModel;
import org.jugvale.certificate.generator.model.Registration;
import org.junit.jupiter.api.Test;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

/**
 * CertificateResourceTest
 */
@QuarkusTest
public class CertificateContentResourceTest {

    private static final String CERTIFICATE_URI = "/certificate";
    private static final String CERTIFICATE_URI_PARAM = CERTIFICATE_URI + "/{id}";
    private static final String CERTIFICATE_CONTENT_URI = CERTIFICATE_URI_PARAM + "/content";
    private static final String CERTIFICATE_GENERATE = CERTIFICATE_URI + "/model/{modelId}/registration/{registrationId}";
    
    private static final String CERTIFICATE_MODEL_URI = "/certificate-model";
    
    private static final String DATA_FETCHERS_URI = "/conference-data-fetchers";
    private static final String DATA_FETCHERS_URI_PARAM = DATA_FETCHERS_URI + "/{name}";
    
    private static final String CERTIFICATE_CONTENT_EMAIL_URI = "certificate-content/{contentId}/send-email";

    @Inject
    MockMailbox mockMailBox;
    
    @Test
    public void testCertificateContentResource() throws Exception {
        mockMailBox.clear();
        createConferenceData();
        String svgFile = CertificateContentResourceTest.class.getResource("/svg/simple.svg").getFile();
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
        
        Certificate certificate = post(CERTIFICATE_GENERATE, model.id, registration.id).then()
                                                                                       .statusCode(200)
                                                                                       .extract()
                                                                                       .as(Certificate.class);
        
        assertEquals(1, mockMailBox.getTotalMessagesSent());
        
        CertificateContent content = given().accept(ContentType.JSON)
                                              .get(CERTIFICATE_CONTENT_URI, certificate.id)
                                              .then()
                                              .extract()
                                              .as(CertificateContent.class);
        
        post(CERTIFICATE_CONTENT_EMAIL_URI, content.id).then().statusCode(200);
        assertEquals(2, mockMailBox.getTotalMessagesSent());
        
        List<Mail> messagesSentTo = mockMailBox.getMessagesSentTo(registration.attendee.email);
        Mail mail = messagesSentTo.get(0);
        
        String formattedParams = String.format("%s %s %s", registration.conference.name, 
                                                           registration.attendee.name, 
                                                           certificate.generationKey);
        String subject = "subject " + formattedParams;
        String body = "body " + formattedParams;
        assertEquals(mail.getSubject(), subject);
        assertEquals(mail.getText(), body);
        assertEquals(1, mail.getAttachments().size());
        // TODO: correctly check the attachment content
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