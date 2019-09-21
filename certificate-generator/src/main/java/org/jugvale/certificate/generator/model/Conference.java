package org.jugvale.certificate.generator.model;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Conference
 */
@Entity
public class Conference extends PanacheEntity {
    
    public Long external_id;
  
    public String name;

    @Override
    public String toString() {
        return "Conference [external_id=" + external_id + ", name=" + name + "]";
    }
}