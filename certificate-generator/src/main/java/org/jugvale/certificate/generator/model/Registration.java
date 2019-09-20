package org.jugvale.certificate.generator.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Registration
 */
@Entity
public class Registration extends PanacheEntity {

  public boolean attendance;

  @ManyToOne
  public Attendee attendee;

  @ManyToOne
  public Conference conference;

  
}