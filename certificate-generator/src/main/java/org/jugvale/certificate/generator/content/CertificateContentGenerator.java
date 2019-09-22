package org.jugvale.certificate.generator.content;

import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateContent;

public interface CertificateContentGenerator {
    
    public CertificateContent generate(Certificate certificate);

}