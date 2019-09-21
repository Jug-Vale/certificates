package org.jugvale.certificate.generator;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CertificateKeyGenerator {

    public String generateKey() {
        return UUID.randomUUID().toString();
    }
}
