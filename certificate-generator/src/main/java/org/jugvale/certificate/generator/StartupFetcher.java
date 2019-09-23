package org.jugvale.certificate.generator;

import java.util.Optional;
import java.util.function.Consumer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.jugvale.certificate.generator.fetcher.ConferenceDataFetcher;
import org.jugvale.certificate.generator.fetcher.ConferenceDataFetcherService;
import org.jugvale.certificate.generator.fetcher.impl.ConfigurationDataFetcher;
import org.jugvale.certificate.generator.model.Registration;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class StartupFetcher {
    
    Logger logger = Logger.getLogger(StartupFetcher.class);
    
    @ConfigProperty(name = "certificate.startup.fetch", defaultValue = "false")
    boolean runFetcher;
    
    @ConfigProperty(name = "certificate.startup.fetcher.name", defaultValue = ConfigurationDataFetcher.NAME)
    String fetcherName;
    
    @Inject
    ConferenceDataFetcherService service;
    
    @Transactional
    public void startup(@Observes StartupEvent ev) {
        
        if (runFetcher) {
            Optional<ConferenceDataFetcher> fetcherOp = service.findFetcher(fetcherName);
            fetcherOp.ifPresentOrElse(e -> {}, () -> logger.infov("Not able to find {0} fetcher", fetcherName)); 
            fetcherOp.stream()
                     .peek(c -> logger.infov("Loading conference data using {0}", c.name()))
                     .map(ConferenceDataFetcher::conferenceData)
                     .peek(c -> Registration.persist(c.getRegistrations()))
                     .findFirst().ifPresent(c -> logger.infov("Finished loading conference data"));
        }
        
        
    }

}
