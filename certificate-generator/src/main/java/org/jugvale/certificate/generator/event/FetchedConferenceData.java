package org.jugvale.certificate.generator.event;

import org.jugvale.certificate.generator.fetcher.ConferenceData;

public class FetchedConferenceData {

    private ConferenceData conferenceData;

    public FetchedConferenceData(ConferenceData conferenceData) {
        this.conferenceData = conferenceData;
    }
    
    public ConferenceData getConferenceData() {
        return conferenceData;
    }

}
