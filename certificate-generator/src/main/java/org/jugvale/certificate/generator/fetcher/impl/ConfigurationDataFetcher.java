package org.jugvale.certificate.generator.fetcher.impl;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.fetcher.ConferenceDataFetcher;
import org.jugvale.certificate.generator.model.Attendee;
import org.jugvale.certificate.generator.model.Conference;
import org.jugvale.certificate.generator.model.Registration;

/**
 * Mocks external data fetching based on configuration
 * 
 * @author wsiqueir
 *
 */
@Alternative
@ApplicationScoped
public class ConfigurationDataFetcher implements ConferenceDataFetcher {
    
    public static final String DESCRIPTION = "Fetch from Configuration";

    @Inject
    @ConfigProperty(name = "example.conference.name", defaultValue = "Mock Conference")
    String conferenceName;
    
    @ConfigProperty(name = "example.attendee.email", defaultValue = "mock@email.com")
    String attendeeEmail;
    
    @ConfigProperty(name = "example.attendee.name", defaultValue = "John")
    String attendeeName;

    private Attendee attendee;

    private Conference conference;

    private Registration registration;
    
    @PostConstruct
    public void initData() {
        conference = new Conference();
        conference.id = 1l;
        conference.name = conferenceName;
        
        attendee = new Attendee();
        attendee.id = 1l;
        attendee.email = attendeeEmail;
        attendee.name = attendeeName;
        
        registration = new Registration();
        registration.attendance = true;
        registration.attendee = attendee;
        registration.conference = conference;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }
    
    @Override
    public ConferenceData conferenceData() {
        return ConferenceData.of(Arrays.asList(conference), 
                                 Arrays.asList(attendee), 
                                 Arrays.asList(registration));
    }

}