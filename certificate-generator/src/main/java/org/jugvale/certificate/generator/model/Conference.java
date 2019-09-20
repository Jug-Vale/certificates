package org.jugvale.certificate.generator.model;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Conference
 */
@Entity
public class Conference extends PanacheEntity {
  
  public String name;
  
}