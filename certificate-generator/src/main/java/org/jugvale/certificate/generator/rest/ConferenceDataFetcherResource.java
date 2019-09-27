package org.jugvale.certificate.generator.rest;

import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.fetcher.ConferenceDataFetcher;
import org.jugvale.certificate.generator.fetcher.ConferenceDataFetcherService;
import org.jugvale.certificate.generator.model.Registration;

@Path("conference-data-fetchers")
@Produces(MediaType.APPLICATION_JSON)
public class ConferenceDataFetcherResource {

    @Inject
    ConferenceDataFetcherService fetcherService;
    
    @GET
    public Map<String, String> fetchers() {
        return fetcherService.fetchers();
    }
    
    @POST
    @Path("{fetcherName}")
    @Transactional
    public Response fetchData(@PathParam("fetcherName") String fetcherName) {
        Optional<ConferenceDataFetcher> fetcherOp = fetcherService.findFetcher(fetcherName);
        ConferenceDataFetcher fetcher = ResourceUtils.exceptionIfNotPresent(fetcherOp, "", Status.NOT_FOUND);
        ConferenceData conferenceData = fetcher.conferenceData();
        conferenceData.getRegistrations().forEach(Registration::merge);
        return Response.ok(conferenceData).build();
    }
    
    @DELETE
    @Transactional
    public void clear() {
        Registration.deleteAll();
    }
    
}