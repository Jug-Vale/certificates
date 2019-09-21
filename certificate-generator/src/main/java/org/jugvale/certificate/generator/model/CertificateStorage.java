package org.jugvale.certificate.generator.model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class CertificateStorage extends PanacheEntity {
    
    @Lob
    public String content;

    @Lob
    public byte[] contentBin;
    
    @ManyToOne
    public Certificate certificate;
}
