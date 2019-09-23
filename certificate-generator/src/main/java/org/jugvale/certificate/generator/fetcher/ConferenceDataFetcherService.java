package org.jugvale.certificate.generator.fetcher;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
public class ConferenceDataFetcherService {
    
    @Inject
    Instance<ConferenceDataFetcher> dataFetchers;
    
    public Map<String, String> fetchers() {
        return dataFetchers.stream().collect(toMap(ConferenceDataFetcher::name, 
                                                   ConferenceDataFetcher::description));
    }

    public Optional<ConferenceDataFetcher> findFetcher(String fetcherName) {
        return dataFetchers.stream()
                           .filter(f -> f.name().equals(fetcherName))
                           .findFirst();
    }
    
    public void runFetcher() {
        
    }

}
