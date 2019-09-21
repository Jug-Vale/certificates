package org.jugvale.certificate.generator.content;

import javax.enterprise.event.Event;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;

import org.jugvale.certificate.generator.event.NewCertificateEvent;
import org.jugvale.certificate.generator.event.NewCertificateStorageEvent;
import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateStorage;

public class NewCertificateListener {
    
    @Inject
    CertificateContentGenerator contentGenerator;
    
    Event<NewCertificateStorageEvent> newCertificateStorageEvent;
    
    public void generateCertificateContent(@ObservesAsync NewCertificateEvent newCertificateEvent) {
        Certificate certificate = newCertificateEvent.getCertificate();
        CertificateStorage certificateStorage = contentGenerator.generate(certificate);
        newCertificateStorageEvent.fireAsync(new NewCertificateStorageEvent(certificateStorage));
    }

}
