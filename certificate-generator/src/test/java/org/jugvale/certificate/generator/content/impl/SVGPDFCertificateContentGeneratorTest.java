package org.jugvale.certificate.generator.content.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.jugvale.certificate.generator.model.Attendee;
import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateModel;
import org.jugvale.certificate.generator.model.CertificateStorage;
import org.jugvale.certificate.generator.model.Registration;
import org.junit.jupiter.api.Test;

public class SVGPDFCertificateContentGeneratorTest {

    @Test
    public void testGenerateStorage() throws IOException {
        
        String svgFile = SVGPDFCertificateContentGeneratorTest.class.getResource("/svg/simple.svg").getFile();
        
        CertificateModel model = new CertificateModel();
        model.attendeeNameField = "attendeeName";
        model.certificateKeyField = "certificateKey";
        model.content = new String(Files.readAllBytes(Paths.get(svgFile)), StandardCharsets.UTF_8);
        
        Certificate certificate = new Certificate();
        
        certificate.certificateModel = model;
        certificate.generationKey = UUID.randomUUID().toString();
        certificate.registration = new Registration();
        certificate.registration.attendee = new Attendee();
        certificate.registration.attendee.name = "Antonio Smith";

        SVGPDFCertificateContentGenerator generator = new SVGPDFCertificateContentGenerator();
        generator.init();
        CertificateStorage generatedContent = generator.generate(certificate);
        
        // possibly better test the pdf and the generated svg
        assertTrue(generatedContent.content.contains(certificate.registration.attendee.name));
        assertTrue(generatedContent.content.contains(certificate.generationKey));
        assertNotNull(generatedContent.contentBin);
        
    }
}
