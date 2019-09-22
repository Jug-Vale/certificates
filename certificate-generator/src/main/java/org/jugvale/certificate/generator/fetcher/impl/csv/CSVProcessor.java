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

import javax.enterprise.context.ApplicationScoped;

import org.jugvale.certificate.generator.fetcher.ConferenceData;
import org.jugvale.certificate.generator.model.Attendee;
import org.jugvale.certificate.generator.model.Conference;
import org.jugvale.certificate.generator.model.Registration;

@ApplicationScoped
public class CSVProcessor {
    
    private static final int TOTAL_COLUMNS = 5;
    
    private static final int CONFERENCE_EXTERNAL_ID_IDX = 0;
    private static final int CONFERENCE_NAME_IDX = 1;
    private static final int ATTENDEE_NAME_IDX = 2;
    private static final int ATTENDEE_EMAIL_IDX = 3;
    private static final int REGISTRATION_ATTENDANCE_IDX = 4;
    
    
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
        Long confId = getLongValue(CONFERENCE_EXTERNAL_ID_IDX, columns);
        
        Conference conference = conferenceData.getConferences().stream()
                                                    .filter(c -> c.externalId.equals(confId))
                                                    .findFirst().orElseGet(() -> {
            Conference newConference = new Conference();
            newConference.externalId = getLongValue(CONFERENCE_EXTERNAL_ID_IDX, columns);
            newConference.name = getStringValue(CONFERENCE_NAME_IDX, columns);
            conferenceData.getConferences().add(newConference);
            return newConference;
        });
        
        String email = getStringValue(ATTENDEE_EMAIL_IDX, columns);
        
        Attendee attendee = conferenceData.getAttendees().stream()
                                                          .filter(a -> a.email.equals(email))
                                                          .findFirst().orElseGet(() -> {
                    Attendee newAttendee = new Attendee();
                    newAttendee.name = getStringValue(ATTENDEE_NAME_IDX, columns);
                    newAttendee.email = getStringValue(ATTENDEE_EMAIL_IDX, columns);
                    conferenceData.getAttendees().add(newAttendee);
                    return newAttendee;
        });
        
        Boolean registration_attendance = getBooleanValue(REGISTRATION_ATTENDANCE_IDX, columns);
        
        Registration registration = new Registration();
        registration.attendance = registration_attendance;
        registration.attendee = attendee;
        registration.conference = conference;
        conferenceData.getRegistrations().add(registration);
    }
}
