package org.jugvale.certificate.generator.model;

/**
 * 
 * Used to summarize registration information
 * 
 * @author wsiqueir
 *
 */
public class RegistrationSummary {
    
    
    private Long registrationId;
    private Long attendeeId;
    private Long conferenceExternalId;
    private String conferenceName;
    private String attendeeName;
    
    public static RegistrationSummary from(Registration registration) {
        RegistrationSummary summary = new RegistrationSummary();
        summary.attendeeId = registration.attendee.id;
        summary.registrationId = registration.id;
        summary.conferenceExternalId = registration.conference.externalId;
        summary.conferenceName = registration.conference.name;
        summary.attendeeName = registration.attendee.name;
        return summary;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public Long getAttendeeId() {
        return attendeeId;
    }

    public Long getConferenceExternalId() {
        return conferenceExternalId;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public String getAttendeeName() {
        return attendeeName;
    }
    
}
