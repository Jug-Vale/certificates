package org.jugvale.certificate.generator.fetcher;

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

    List<Conference> getConferences(){
        return conferences;
    }
    
    List<Attendee> attendees() {
        return attendees;
    }
    
    List<Registration> registrations() {
        return registrations;
    }

}
