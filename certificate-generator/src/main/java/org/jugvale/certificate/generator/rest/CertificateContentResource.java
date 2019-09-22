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
import org.jugvale.certificate.generator.model.CertificateContent;

@Path("certificate-content")
@Produces(MediaType.APPLICATION_JSON)
public class CertificateContentResource {
    
    @Inject 
    EmailService emailService;
    
    @POST
    @Path("{contentId}/send-email")
    public Response email(@PathParam("contentId") Long contentId) {
        CertificateContent content = CertificateContent.findById(contentId);
        ResourceUtils.exceptionIfNull(content, "Content does not exist", Status.NOT_FOUND);
        emailService.send(content);
        return Response.ok().build();
    }
    
}