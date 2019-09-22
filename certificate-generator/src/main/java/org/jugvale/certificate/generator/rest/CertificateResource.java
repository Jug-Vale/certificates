package org.jugvale.certificate.generator.rest;

import java.util.Optional;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.jugvale.certificate.generator.CertificateKeyGenerator;
import org.jugvale.certificate.generator.event.DeletedCertificateEvent;
import org.jugvale.certificate.generator.event.NewCertificateEvent;
import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateModel;
import org.jugvale.certificate.generator.model.Registration;

@Path("certificate")
@Produces(MediaType.APPLICATION_JSON)
public class CertificateResource {
    
    @Inject
    CertificateKeyGenerator keyGenerator;
    
    @Inject
    Event<NewCertificateEvent> newCertificateEvent;
    
    @Inject
    Event<DeletedCertificateEvent> deletedCertificateEvent;
    
    @POST
    @Transactional
    @Path("/model/{modelId}/registration/{registrationId}")
    public Certificate generate(@PathParam("modelId") Long modelId,
                                @PathParam("registrationId") Long registrationId,
                                @QueryParam("force") boolean force) {
        
        Registration registration = Registration.findById(registrationId);
        CertificateModel model = CertificateModel.findById(modelId);
        ResourceUtils.exceptionIfNull(model, "Certification Model does not exist", Status.PRECONDITION_FAILED);
        ResourceUtils.exceptionIfNull(registration, "Registration does not exist", Status.PRECONDITION_FAILED);
        Optional<?> certificateOp = Certificate.find("registration", registration)
                                               .list()
                                               .stream()
                                               .filter(c -> !force)
                                               .findAny();
        ResourceUtils.exceptionIfPresent(certificateOp, "Certificate for registration already generated", Status.CONFLICT);
        
        Certificate certificate = new Certificate();
        certificate.certificateModel = model;
        certificate.registration = registration;
        certificate.generationKey = keyGenerator.generateKey();
        
        Certificate.persist(certificate);
        
        newCertificateEvent.fireAsync(new NewCertificateEvent(certificate));
        
        return certificate;
    }
    
    @DELETE
    @Path("{id}")
    @Transactional
    public void remove(@PathParam("id") Long id) {
        Certificate certificate = Certificate.findById(id);
        ResourceUtils.exceptionIfNull(certificate, "Certificate not found", Status.NOT_FOUND);
        deletedCertificateEvent.fireAsync(new DeletedCertificateEvent(certificate))
                               .thenApply(e -> Certificate.delete("id", id));
    }
        
}
