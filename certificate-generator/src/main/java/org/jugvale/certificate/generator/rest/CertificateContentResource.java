package org.jugvale.certificate.generator.rest;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jugvale.certificate.generator.email.EmailService;
import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateContent;
import org.jugvale.certificate.generator.model.Registration;

@Path("certificate-content")
@Produces(MediaType.APPLICATION_JSON)
public class CertificateContentResource {
    
    @Inject 
    EmailService emailService;
    
    @POST
    @Path("{contentId}/send-email")
    public Response emailByContent(@PathParam("contentId") Long contentId) {
        CertificateContent content = CertificateContent.findById(contentId);
        return verifyContentAndSendEmail(content);
    }
    
    @POST
    @Path("registration/{registrationId}/send-email")
    public Response emailByRegistration(@PathParam("registrationId") Long registrationId) {
        Registration registration = Registration.findById(registrationId);
        ResourceUtils.exceptionIfNull(registration, "Registration does not exist", Status.NOT_FOUND);
        Certificate certificate = Certificate.find("registration", registration).firstResult();
        ResourceUtils.exceptionIfNull(registration, "Certificate not generated for registration", Status.PRECONDITION_FAILED);
        return findContentAndSendEmail(certificate);
    }
    
    @POST
    @Path("certificate/{certificateId}/send-email")
    public Response emailByCertificate(@PathParam("certificateId") Long certificateId) {
        Certificate certificate = Certificate.findById(certificateId);
        ResourceUtils.exceptionIfNull(certificate, "Certificate does not exist", Status.NOT_FOUND);
        return findContentAndSendEmail(certificate);
    }

    private Response findContentAndSendEmail(Certificate certificate) {
        CertificateContent content = CertificateContent.find("certificate", certificate)
                                                       .firstResult();
        return verifyContentAndSendEmail(content);
    }

    private Response verifyContentAndSendEmail(CertificateContent content) {
        ResourceUtils.exceptionIfNull(content, "Content does not exist", Status.NOT_FOUND);
        emailService.send(content);
        return Response.ok().build();
    }
    
}