package org.jugvale.certificate.generator.event;

import org.jugvale.certificate.generator.model.Certificate;

public class DeletedCertificateEvent {
    
    private Certificate certificate;

    public DeletedCertificateEvent(Certificate certificate) {
        this.certificate = certificate;
    }
    
    public Certificate getCertificate() {
        return certificate;
    }
}