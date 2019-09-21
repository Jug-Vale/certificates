package org.jugvale.certificate.generator.content.consumers;

import org.jugvale.certificate.generator.model.CertificateStorage;

public class CertificateDatabaseStorageConsumer extends CertificateStorageConsumer {

    @Override
    protected void consume(CertificateStorage storage) {
        CertificateStorage.persist(storage);
    }

}