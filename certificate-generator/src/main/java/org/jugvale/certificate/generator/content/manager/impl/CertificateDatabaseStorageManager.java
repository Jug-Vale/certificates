package org.jugvale.certificate.generator.content.manager.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.jugvale.certificate.generator.content.manager.CertificateStorageManager;
import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateContent;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@ApplicationScoped
public class CertificateDatabaseStorageManager implements CertificateStorageManager {

    @Override
    @Transactional
    public void storeCertificate(CertificateContent storage) {
        System.out.println("PERSISTING " + storage.content);
        storage.persist();
    }

    public String name() {
        return "database";
    }

    @Override
    @Transactional
    public void removeStorageForCertificate(Certificate certificate) {
        CertificateContent.find("certificate", certificate)
                          .list()
                          .forEach(PanacheEntityBase::delete);
    }

}