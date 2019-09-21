package org.jugvale.certificate.generator.event;

import org.jugvale.certificate.generator.model.Certificate;

public class NewCertificateEvent {
    
    private Certificate certificate;

    public NewCertificateEvent(Certificate certificate) {
        this.certificate = certificate;
    }
    
    public Certificate getCertificate() {
        return certificate;
    }
}