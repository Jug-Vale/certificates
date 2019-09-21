package org.jugvale.certificate.generator.content;

import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateStorage;

public interface CertificateContentGenerator {
    
    public CertificateStorage generate(Certificate certificate);

}