package org.jugvale.certificate.generator.fetcher;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;

import org.jugvale.certificate.generator.event.FetchedConferenceData;
import org.jugvale.certificate.generator.model.Registration;

@ApplicationScoped
public class DataFetchListener {
    
    @Transactional
    public void storeFetchedData(@Observes FetchedConferenceData fetchedConferenceData) {
        ConferenceData conferenceData = fetchedConferenceData.getConferenceData();
        persistConferenceData(conferenceData);
    }

    private void persistConferenceData(ConferenceData conferenceData) {
        System.out.println("PERSISINGINT " + conferenceData.getRegistrations());
        Registration.persist(conferenceData.getRegistrations());
    }

}
