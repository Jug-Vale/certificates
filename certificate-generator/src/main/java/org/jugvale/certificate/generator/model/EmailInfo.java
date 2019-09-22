package org.jugvale.certificate.generator.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Tracks emails sent
 * 
 * @author wsiqueir
 *
 */
@Entity
public class EmailInfo extends PanacheEntity {
    
    public Date sentDate;
    
    public String subject;
    
    @Lob
    public String body;
    
    @ManyToOne
    public CertificateContent certificateContent;
}
