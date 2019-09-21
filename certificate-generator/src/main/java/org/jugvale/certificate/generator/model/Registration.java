package org.jugvale.certificate.generator.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Registration
 */
@Entity
public class Registration extends PanacheEntity {

    @Id
    public Long id;
    
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
    

}