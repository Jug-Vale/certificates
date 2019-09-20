package org.jugvale.certificate.generator.model;

import javax.persistence.Entity;
import javax.persistence.Lob;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class CertificateModel extends PanacheEntity {

  @Lob
  public String content;
  
  public String attendeeNameField;
  
  public String certificateKeyField;
 
}