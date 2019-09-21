package org.jugvale.certificate.generator.content.consumers;

import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.jugvale.certificate.generator.event.NewCertificateStorageEvent;
import org.jugvale.certificate.generator.model.CertificateStorage;

public abstract class CertificateStorageConsumer {
    
    public static final String CONFIG_TEMPLATE = "certificate.storage.consumer.%s.enable";
    
    @Inject
    Config config;
    
    public void consume(@ObservesAsync NewCertificateStorageEvent certificateStorageEvent ) {
        String className = this.getClass().getName();
        String prop = String.format(CONFIG_TEMPLATE, className);
        Boolean enabled = config.getValue(prop, Boolean.class);
        if (enabled) {
            consume(certificateStorageEvent.getCertificateStorage());
        }
    }
    
    protected abstract void consume(CertificateStorage storage);

}
