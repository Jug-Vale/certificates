package org.jugvale.certificate.generator.fetcher.impl.csv;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.fetcher.ConferenceDataFetcher;

/**
 * 
 * Fetches Conference data from a CSV file
 * @author wsiqueir
 *
 */
@Alternative
@ApplicationScoped
public class CSVConferenceDataFetcher implements ConferenceDataFetcher {
    
    public static String DESCRIPTION = "Fetches data from CSV file";

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public ConferenceData conferenceData() {
        return null;
    }

}
