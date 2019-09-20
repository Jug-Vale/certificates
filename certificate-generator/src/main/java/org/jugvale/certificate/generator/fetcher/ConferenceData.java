package org.jugvale.certificate.generator.fetcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jugvale.certificate.generator.model.Attendee;
import org.jugvale.certificate.generator.model.Conference;
import org.jugvale.certificate.generator.model.Registration;

public class ConferenceData {
    
    private List<Conference> conferences;
    private List<Attendee> attendees;
    private List<Registration> registrations;
    
    private ConferenceData(List<Conference> conferences, List<Attendee> attendees, List<Registration> registrations) {
        this.conferences = conferences;
        this.attendees = attendees;
        this.registrations = registrations;
    }

    public static ConferenceData of(List<Conference> conferences, 
                                    List<Attendee> attendees, 
                                    List<Registration> registrations) {
        return new ConferenceData(conferences, attendees, registrations);
    }
    
    public static ConferenceData empty() {
        return new ConferenceData(Collections.emptyList(), 
                                  Collections.emptyList(), 
                                  Collections.emptyList());
    }
    
    public static ConferenceData create() {
        return new ConferenceData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public List<Conference> getConferences(){
        return conferences;
    }
    
    public List<Attendee> getAttendees() {
        return attendees;
    }
    
    public List<Registration> getRegistrations() {
        return registrations;
    }

}
