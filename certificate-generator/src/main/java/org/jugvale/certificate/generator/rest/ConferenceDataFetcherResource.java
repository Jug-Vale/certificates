package org.jugvale.certificate.generator.rest;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Optional;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jugvale.certificate.generator.event.FetchedConferenceData;
import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.fetcher.ConferenceDataFetcher;

@Path("conference-data-fetchers")
@Produces(MediaType.APPLICATION_JSON)
public class ConferenceDataFetcherResource {
    
    @Inject
    Instance<ConferenceDataFetcher> dataFetchers;
    
    @Inject
    Event<FetchedConferenceData> fetchedConferenceDataEvent;
    
    @GET
    public Map<String, String> fetchers() {
        return dataFetchers.stream().collect(toMap(ConferenceDataFetcher::name, 
                                                   ConferenceDataFetcher::description));
    }
    
    @POST
    @Path("{fetcherName}")
    public Response fetchData(@PathParam("fetcherName") String fetcherName) {
        Optional<ConferenceDataFetcher> fetcherOp = dataFetchers.stream()
                                                                .filter(f -> f.name().equals(fetcherName))
                                                                .findFirst();
        
        ConferenceDataFetcher fetcher = fetcherOp.orElseThrow(() -> new WebApplicationException(Status.NOT_FOUND));
        
        ConferenceData conferenceData = fetcher.conferenceData();
        
        fetchedConferenceDataEvent.fire(new FetchedConferenceData(conferenceData));
        
        return Response.ok(conferenceData).build();
    }
    
}