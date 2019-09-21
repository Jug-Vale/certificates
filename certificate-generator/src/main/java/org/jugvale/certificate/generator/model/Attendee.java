package org.jugvale.certificate.generator.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Attendee
 */
@Entity
public class Attendee extends PanacheEntity {

    @Id
    public Long id;
    
    public String name;

    public String email;

    @Override
    public String toString() {
        return "Attendee [id=" + id + ", name=" + name + ", email=" + email + "]";
    }

}