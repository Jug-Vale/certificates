package org.jugvale.certificate.generator.fetcher.impl.csv;

import static org.jugvale.certificate.generator.fetcher.impl.csv.CSVUtilities.getBooleanValue;
import static org.jugvale.certificate.generator.fetcher.impl.csv.CSVUtilities.getLongValue;
import static org.jugvale.certificate.generator.fetcher.impl.csv.CSVUtilities.getStringValue;
import static org.jugvale.certificate.generator.fetcher.impl.csv.CSVUtilities.columns;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.fetcher.ConferenceDataFetcher;
import org.jugvale.certificate.generator.model.Attendee;
import org.jugvale.certificate.generator.model.Conference;
import org.jugvale.certificate.generator.model.Registration;

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
    public ConferenceData conferenceData() {
        if (csvConfFilePath.isEmpty()) {
            logger.infov("CSV file path {} not specified.", FILE_PROP);
            return ConferenceData.empty();
        }
        return CSVProcessor.processCSV(csvConfFilePath);
    }
    
}
