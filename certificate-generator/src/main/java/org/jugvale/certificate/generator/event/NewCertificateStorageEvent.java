package org.jugvale.certificate.generator.event;

import org.jugvale.certificate.generator.model.CertificateStorage;

public class NewCertificateStorageEvent {
    
    private CertificateStorage certificateStorage;

    public NewCertificateStorageEvent(CertificateStorage certificateStorage) {
        this.certificateStorage = certificateStorage;
    }
    
    public CertificateStorage getCertificateStorage() {
        return certificateStorage;
    }

}