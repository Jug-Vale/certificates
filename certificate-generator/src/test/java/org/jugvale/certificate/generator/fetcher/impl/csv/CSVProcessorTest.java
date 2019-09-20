package org.jugvale.certificate.generator.fetcher.impl.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.model.Attendee;
import org.jugvale.certificate.generator.model.Conference;
import org.jugvale.certificate.generator.model.Registration;
import org.junit.jupiter.api.Test;

public class CSVProcessorTest {
    
    private CSVProcessor processor = new CSVProcessor();

    @Test 
    public void testGoodCSV() {
        String goodCSVPath = CSVProcessorTest.class
                                             .getResource("/csv/good_data.csv")
                                             .getFile();
        ConferenceData conferenceData = processor.processCSV(goodCSVPath);
        
        
        assertEquals(1, conferenceData.getConferences().size());
        
        Conference conference = conferenceData.getConferences().get(0);
        
        assertEquals("The big IT conf", conference.name);
        assertEquals(1, conference.id);
        
        List<Attendee> attendees = conferenceData.getAttendees();
        assertEquals(3, attendees.size());
        
        assertEquals(1, attendees.get(0).id);
        assertEquals("antonio@email.com", attendees.get(0).email);
        assertEquals("Antonio Camara", attendees.get(0).name);
        
        assertEquals(2, attendees.get(1).id);
        assertEquals("luana@email.com", attendees.get(1).email);
        assertEquals("Luana Camara", attendees.get(1).name);
        
        
        assertEquals(3, attendees.get(2).id);
        assertEquals("william@email.com", attendees.get(2).email);
        assertEquals("William", attendees.get(2).name);
        
        
        List<Registration> registrations = conferenceData.getRegistrations();
        
        assertEquals(3, registrations.size());
        
        assertTrue(registrations.get(0).attendance);
        assertEquals(1, registrations.get(0).id);
        assertTrue(registrations.get(1).attendance);
        assertEquals(2, registrations.get(1).id);
        assertTrue(registrations.get(2).attendance);
        assertEquals(3, registrations.get(2).id);
        
    }
    
    @Test 
    public void testBadsCSVs() {
        String badColumnsCSVPath = CSVProcessorTest.class
                                                   .getResource("/csv/bad_columns.csv")
                                                   .getFile();
        String duplicateRegistrationCSVPath = CSVProcessorTest.class
                                                              .getResource("/csv/duplicate_registration.csv")
                                                              .getFile();
        
        assertThrows(RuntimeException.class, () -> processor.processCSV(badColumnsCSVPath));
        
        assertThrows(RuntimeException.class, () -> processor.processCSV(duplicateRegistrationCSVPath));
        
    }
}
