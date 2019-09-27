package org.jugvale.certificate.generator.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Conference
 */
@Entity
public class Conference extends PanacheEntity {
    
    @Column(unique = true)
    public Long externalId;
  
    public String name;

    @Override
    public String toString() {
        return "Conference [external_id=" + externalId + ", name=" + name + "]";
    }
    
    public static Conference merge(Conference conference) {
        Conference.find("externalId", conference.externalId)
                 .stream().findFirst()
                 .ifPresent(existingConference -> conference.id = ((Conference) existingConference).id);
        return Panache.getEntityManager().merge(conference);
    }
}