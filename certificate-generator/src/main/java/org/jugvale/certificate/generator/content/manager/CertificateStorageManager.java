package org.jugvale.certificate.generator.content.manager;

import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateContent;

public interface CertificateStorageManager {
    
    void storeCertificate(CertificateContent storage);
    
    void removeStorageForCertificate(Certificate storage);
    
    default String name() {
        return this.getClass().getSimpleName();
    }

}
