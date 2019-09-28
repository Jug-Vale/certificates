package org.jugvale.certificate.generator.model;

import java.sql.Date;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Certificate
 */
@Entity
public class Certificate extends PanacheEntity {

  @Column(length = 100, unique = true)
  public String generationKey;

  @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  public Date lastModified;

  @ManyToOne
  public CertificateModel certificateModel;
  
  @ManyToOne
  public Registration registration;

  public static Certificate with(Registration registration) {
      Certificate certificate = new Certificate();
      certificate.lastModified = new Date(System.currentTimeMillis());
      certificate.registration = registration;
      return certificate;
  }

}