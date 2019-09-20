package org.jugvale.certificate.generator.model;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Attendee
 */
@Entity
public class Attendee extends PanacheEntity {

  public String name;

  public String email;
  
}