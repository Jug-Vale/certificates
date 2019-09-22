package org.jugvale.certificate.generator.content.listener.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.jugvale.certificate.generator.content.listener.CertificateStorageListener;
import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateContent;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@ApplicationScoped
public class CertificateDatabaseStorageManager implements CertificateStorageListener {

    @Override
    @Transactional
    public void newCertificateContent(CertificateContent storage) {
        storage.persist();
    }

    public String name() {
        return "database";
    }

    @Override
    @Transactional
    public void removedCertificateContent(Certificate certificate) {
        CertificateContent.find("certificate", certificate)
                          .list()
                          .forEach(PanacheEntityBase::delete);
    }

}