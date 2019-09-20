package org.jugvale.certificate.generator.fetcher;

/**
 * 
 * Interface to access base data 
 * 
 *
 */
public interface ConferenceDataFetcher {

    String description();
    
    ConferenceData  conferenceData();
    
}