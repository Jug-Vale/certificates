package org.jugvale.certificate.generator.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jugvale.certificate.generator.fetcher.ConferenceDataFetcher;

@Path("conference-data-fetchers")
@Produces(MediaType.APPLICATION_JSON)
public class ConferenceDataFetcherResource {
    

    @Inject
    Instance<ConferenceDataFetcher> dataFetchers;
    
    @GET
    public List<String> fetchers() {
        return dataFetchers.stream().map(ConferenceDataFetcher::description).collect(Collectors.toList());
    }
    
}