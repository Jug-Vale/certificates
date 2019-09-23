package org.jugvale.certificate.generator.model;

public class CertificateSummary {

    private String key;
    private Long conferenceId;
    private String conferenceName;
    private String attendeeName;
    private String attendeeEmail;
    
    public CertificateSummary() {}

    private CertificateSummary(String key, Long conferenceId, String conferenceName, String attendeeName, String attendeeEmail) {
        this.key = key;
        this.conferenceId = conferenceId;
        this.conferenceName = conferenceName;
        this.attendeeName = attendeeName;
        this.attendeeEmail = attendeeEmail;
    }
    
    public static CertificateSummary of(Certificate certificate) {
        return new CertificateSummary(
                certificate.generationKey,
                certificate.registration.conference.externalId,
                certificate.registration.conference.name,
                certificate.registration.attendee.name,
                certificate.registration.attendee.email
        );
    }
    
    public String getKey() {
        return key;
    }

    public Long getConferenceId() {
        return conferenceId;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public String getAttendeeName() {
        return attendeeName;
    }

    public String getAttendeeEmail() {
        return attendeeEmail;
    }


    
}