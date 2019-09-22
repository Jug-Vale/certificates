package org.jugvale.certificate.generator.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.jugvale.certificate.generator.model.Registration;
import org.jugvale.certificate.generator.model.RegistrationSummary;

@Path("registration")
@Produces(MediaType.APPLICATION_JSON)
public class RegistrationResource {
    
    @GET
    @Path("/external_conference_id/{externalConferenceId}")
    public List<RegistrationSummary> registrationsForEvent(@PathParam("externalConferenceId") Long externalConferenceId) {
        List<Registration> registrations = Registration.find("conference.externalId", externalConferenceId).list();
        ResourceUtils.exceptionIfEmpty(registrations, "No registration found for conference", Status.NOT_FOUND);
        return registrations.stream().map(RegistrationSummary::from).collect(Collectors.toList());
        
    }
}
