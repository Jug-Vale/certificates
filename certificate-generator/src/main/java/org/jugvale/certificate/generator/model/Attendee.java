package org.jugvale.certificate.generator.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Attendee
 */
@Entity
public class Attendee extends PanacheEntity {
    
    public String name;

    @Column(unique=true)
    public String email;

    @Override
    public String toString() {
        return "Attendee [id=" + id + ", name=" + name + ", email=" + email + "]";
    }
    
    public static Attendee merge(Attendee attendee) {
        Attendee.stream("email", attendee.email)
                .findFirst()
                .ifPresent(existingAttendee -> attendee.id  = ((Attendee) existingAttendee).id);
        return Panache.getEntityManager().merge(attendee);
    }

}