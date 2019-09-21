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

    public static final String NAME = "Configuration";

    public static final long DEFAULT_EXTERNAL_CONF_ID = 1l;
    public static final String DEFAULT_CONFERENCE_NAME = "Mock Conference";
    public static final String DEFAULT_ATTENDEE_NAME = "John";
    public static final String DEFAULT_ATTENDEE_EMAIL = "mock@email.com";

    @Inject
    @ConfigProperty(name = "example.conference.name", defaultValue = DEFAULT_CONFERENCE_NAME)
    String conferenceName;
    
    @ConfigProperty(name = "example.attendee.email", defaultValue = DEFAULT_ATTENDEE_EMAIL)
    String attendeeEmail;
    
    @ConfigProperty(name = "example.attendee.name", defaultValue = DEFAULT_ATTENDEE_NAME)
    String attendeeName;

    private Attendee attendee;

    private Conference conference;

    private Registration registration;

    private ConferenceData conferenceData;

    private ConferenceData staticConferenceData;
    
    @Override
    public String name() {
        return NAME;
    }
    
    @PostConstruct
    public void initData() {
        conferenceData = buildConferenceData(DEFAULT_EXTERNAL_CONF_ID, conferenceName, attendeeEmail, attendeeName);
        staticConferenceData = buildConferenceData(DEFAULT_EXTERNAL_CONF_ID, DEFAULT_CONFERENCE_NAME, DEFAULT_ATTENDEE_NAME, DEFAULT_ATTENDEE_EMAIL);
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }
    
    @Override
    public ConferenceData conferenceData() {
        return conferenceData;
    }
    
    public ConferenceData staticConferenceData() {
        return staticConferenceData;
    }
    
    private ConferenceData buildConferenceData(Long extenalId, 
                                               String conferenceName,
                                               String attendeeEmail,
                                               String attendeeName) {
        conference = new Conference();
        conference.external_id = 1l;
        conference.name = conferenceName;
        
        attendee = new Attendee();
        attendee.email = attendeeEmail;
        attendee.name = attendeeName;
        
        registration = new Registration();
        registration.attendance = true;
        registration.attendee = attendee;
        registration.conference = conference;
        return ConferenceData.of(Arrays.asList(conference), 
                                 Arrays.asList(attendee), 
                                 Arrays.asList(registration));
    }
    
}