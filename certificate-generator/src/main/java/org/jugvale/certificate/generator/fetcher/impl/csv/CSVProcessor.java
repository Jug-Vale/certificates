package org.jugvale.certificate.generator.fetcher.impl.csv;

import static org.jugvale.certificate.generator.fetcher.impl.csv.CSVUtilities.columns;
import static org.jugvale.certificate.generator.fetcher.impl.csv.CSVUtilities.getBooleanValue;
import static org.jugvale.certificate.generator.fetcher.impl.csv.CSVUtilities.getLongValue;
import static org.jugvale.certificate.generator.fetcher.impl.csv.CSVUtilities.getStringValue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.model.Attendee;
import org.jugvale.certificate.generator.model.Conference;
import org.jugvale.certificate.generator.model.Registration;

public class CSVProcessor {
    
    private static final int TOTAL_COLUMNS = 7;
    
    private static final int CONFERENCE_ID_IDX = 0;
    private static final int CONFERENCE_NAME_IDX = 1;
    private static final int ATTENDEE_ID_IDX = 2;
    private static final int ATTENDEE_NAME_IDX = 3;
    private static final int ATTENDEE_EMAIL_IDX = 4;
    private static final int REGISTRATION_ID_IDX = 5;
    private static final int REGISTRATION_ATTENDANCE_IDX = 6;
    
    
    ConferenceData processCSV(String csvFilePath) {
        Path csvPath = Paths.get(csvFilePath);
        ConferenceData conferenceData = ConferenceData.create();
        AtomicLong lineNumber = new AtomicLong();
        if (! Files.exists(csvPath)) {
            throw new RuntimeException(String.format("File %s does not exist.", csvPath));
        }
        try {
            Files.lines(csvPath, StandardCharsets.UTF_8)
                 .forEach(line -> processCSVLine(conferenceData, lineNumber.incrementAndGet(), line));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error processing CSV file %s at line %d ", csvFilePath, lineNumber.get()));
        }
        
        return conferenceData;

    }

    private void processCSVLine(ConferenceData conferenceData, long lineNumber, String line) {
        String[] columns = columns(line);
        if (columns.length != TOTAL_COLUMNS) {
            throw new RuntimeException(String.format("Wrong number of columns at line %d ", line));
        }
        Long confId = getLongValue(CONFERENCE_ID_IDX, columns);
        
        conferenceData.getConferences().stream()
                                       .filter(c -> c.id.equals(confId))
                                       .findFirst().orElseGet(() -> {
            Conference conference = new Conference();
            conference.id = confId;
            conference.name = getStringValue(CONFERENCE_NAME_IDX, columns);
            conferenceData.getConferences().add(conference);
            return conference;
        });
        
        Long attendeeId = getLongValue(ATTENDEE_ID_IDX, columns);
        
        conferenceData.getAttendees().stream()
                                    .filter(a -> a.id.equals(attendeeId))
                                    .findFirst().orElseGet(() -> {
                    Attendee attendee = new Attendee();
                    attendee.id = attendeeId;
                    attendee.name = getStringValue(ATTENDEE_NAME_IDX, columns);
                    attendee.email = getStringValue(ATTENDEE_EMAIL_IDX, columns);
                    conferenceData.getAttendees().add(attendee);
                    return attendee;
        });
        
        Long registrationId = getLongValue(REGISTRATION_ID_IDX, columns);
        Boolean registration_attendance = getBooleanValue(REGISTRATION_ATTENDANCE_IDX, columns);
        
        conferenceData.getRegistrations().stream()
                                         .filter(r -> r.id.equals(registrationId))
                                         .findAny()
                                         .ifPresent(r ->  { 
                                             throw new RuntimeException(String.format("Duplicate Registration line %d, id %d ", 
                                                                                      line, 
                                                                                      registrationId));
                                         });
        Registration registration = new Registration();
        registration.attendance = registration_attendance;
        registration.id = registrationId;
        conferenceData.getRegistrations().add(registration);
    }
}
