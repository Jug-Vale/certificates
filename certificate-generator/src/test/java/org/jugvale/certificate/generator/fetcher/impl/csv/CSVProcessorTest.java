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
        assertEquals(1, conference.externalId);
        
        List<Attendee> attendees = conferenceData.getAttendees();
        assertEquals(3, attendees.size());
        
        Attendee attendee = attendees.get(0);
        assertEquals("antonio@email.com", attendee.email);
        assertEquals("Antonio Camara", attendee.name);
        
        
        Attendee attendee2 = attendees.get(1);
        assertEquals("luana@email.com", attendee2.email);
        assertEquals("Luana Camara", attendee2.name);
        
        Attendee attendee3 = attendees.get(2);
        assertEquals("william@email.com", attendee3.email);
        assertEquals("William", attendee3.name);
        
        
        List<Registration> registrations = conferenceData.getRegistrations();
        
        assertEquals(3, registrations.size());
        
        Registration registration = registrations.get(0);
        assertTrue(registration.attendance);
        assertEquals(conference, registration.conference);
        assertEquals(attendee, registration.attendee);
        
        Registration registration2 = registrations.get(1);
        assertTrue(registration2.attendance);
        assertEquals(conference, registration2.conference);
        assertEquals(attendee2, registration2.attendee);
        
        Registration registration3 = registrations.get(2);
        assertTrue(registration3.attendance);
        assertEquals(conference, registration3.conference);
        assertEquals(attendee3, registration3.attendee);
        
    }
    
    @Test 
    public void testBadsCSVs() {
        String badColumnsCSVPath = CSVProcessorTest.class
                                                   .getResource("/csv/bad_columns.csv")
                                                   .getFile();
        assertThrows(RuntimeException.class, () -> processor.processCSV(badColumnsCSVPath));
        
    }
}
