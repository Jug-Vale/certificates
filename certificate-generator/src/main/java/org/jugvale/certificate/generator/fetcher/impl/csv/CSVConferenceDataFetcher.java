package org.jugvale.certificate.generator.fetcher.impl.csv;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.fetcher.ConferenceDataFetcher;

/**
 * 
 * Fetches Conference data from a CSV file
 * 
 * @author wsiqueir
 *
 */
@Alternative
@ApplicationScoped
public class CSVConferenceDataFetcher implements ConferenceDataFetcher {

    public static final String DESCRIPTION = "Fetches data from CSV file";
    public static final String NAME = "CSV";
    public static final String FILE_PROP = "certificate.fetcher.csv.file";
    
    Logger logger = Logger.getLogger(CSVConferenceDataFetcher.class);

    @Inject
    @ConfigProperty(name = FILE_PROP, defaultValue = "")
    String csvConfFilePath;
    
    @Inject
    CSVProcessor CSVProcessor;
    
    @Override
    public String description() {
        return DESCRIPTION;
    }
    
    @Override
    public String name() {
        return NAME;
    }

    @Override
    public ConferenceData conferenceData() {
        if (csvConfFilePath.isEmpty()) {
            logger.infov("CSV file path {0} not specified.", FILE_PROP);
            return ConferenceData.empty();
        }
        return CSVProcessor.processCSV(csvConfFilePath);
    }

}
