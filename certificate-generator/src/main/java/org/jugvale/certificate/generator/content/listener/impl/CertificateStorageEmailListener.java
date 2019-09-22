package org.jugvale.certificate.generator.content.listener.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jugvale.certificate.generator.content.listener.CertificateStorageListener;
import org.jugvale.certificate.generator.email.EmailService;
import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateContent;

@ApplicationScoped
public class CertificateStorageEmailListener implements CertificateStorageListener {
    
    @Inject
    EmailService emailService;

    @Override
    public void newCertificateContent(CertificateContent content) {
        emailService.send(content);
    }

    @Override
    public void removedCertificateContent(Certificate content) {
    }
    
    @Override
    public String name() {
        return "email";
    }

}
