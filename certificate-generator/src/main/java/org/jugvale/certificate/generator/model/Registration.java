package org.jugvale.certificate.generator.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Registration
 */
@Entity
public class Registration extends PanacheEntity {
    
    public boolean attendance;

    @ManyToOne(cascade = CascadeType.ALL)
    public Attendee attendee;

    @ManyToOne(cascade = CascadeType.ALL)
    public Conference conference;

    @Override
    public String toString() {
        return "Registration [id=" + id + ", attendance=" + attendance + ", attendee=" + attendee + ", conference="
                + conference + "]";
    }
    
    public static void merge(Registration registration) {
        registration.attendee = Attendee.merge(registration.attendee);
        registration.conference = Conference.merge(registration.conference);
        Registration.find("attendee = ?1 and conference = ?2",  registration.attendee, registration.conference)
                    .stream().findFirst()
                    .ifPresentOrElse(r ->  {
                        Registration existingRegistration = (Registration) r;
                        existingRegistration.attendance = registration.attendance; 
                        r.persist();
                        registration.id = existingRegistration.id;
                      }, registration::persist);
    }
    

}