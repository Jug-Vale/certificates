package org.jugvale.certificate.generator.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Certificate
 */
@Entity
public class Certificate extends PanacheEntity {

  @Column(length = 100)
  public String generationKey;

  public Date lastModified;

  @ManyToOne
  public CertificateModel certificateModel;

}