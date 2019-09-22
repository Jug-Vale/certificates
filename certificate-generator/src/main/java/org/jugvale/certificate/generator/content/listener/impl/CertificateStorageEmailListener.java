package org.jugvale.certificate.generator.content.listener.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jugvale.certificate.generator.content.listener.CertificateStorageListener;
import org.jugvale.certificate.generator.email.EmailService;
import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateContent;
import org.jugvale.certificate.generator.model.EmailInfo;

@ApplicationScoped
public class CertificateStorageEmailListener implements CertificateStorageListener {
    
    @Inject
    EmailService emailService;

    @Override
    public void newCertificateContent(CertificateContent content) {
        emailService.send(content);
    }

    @Override
    @Transactional
    public void removedCertificateContent(Certificate certificate) {
        EmailInfo.delete("certificateId", certificate.id);
    }
    
    @Override
    public String name() {
        return "email";
    }

}
