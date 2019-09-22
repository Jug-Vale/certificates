package org.jugvale.certificate.generator.content;

import javax.enterprise.event.ObservesAsync;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.jugvale.certificate.generator.content.manager.CertificateStorageManager;
import org.jugvale.certificate.generator.event.DeletedCertificateEvent;
import org.jugvale.certificate.generator.event.NewCertificateEvent;
import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateContent;

public class NewCertificateListener {

    public static final String CONFIG_TEMPLATE = "certificate.storage.consumer.%s.enable";
    
    @Inject
    CertificateContentGenerator contentGenerator;
    
    @Inject
    Config config;
    
    @Inject
    Instance<CertificateStorageManager> storageConsumers;
    
    public void generateCertificateContent(@ObservesAsync NewCertificateEvent newCertificateEvent) {
        System.out.println("NEW CERTIFICATE!");
        Certificate certificate = newCertificateEvent.getCertificate();
        CertificateContent certificateStorage = contentGenerator.generate(certificate);
        storageConsumers.stream().filter(this::filterConsumer).forEach(c -> c.storeCertificate(certificateStorage));
    }
    
    public void removeCertificateContent(@ObservesAsync DeletedCertificateEvent deletedCertificateEvent) {
        System.out.println("NEW CERTIFICATE!");
        Certificate certificate = deletedCertificateEvent.getCertificate();
        storageConsumers.stream().filter(this::filterConsumer).forEach(c -> c.removeStorageForCertificate(certificate));
    }
    
    public boolean filterConsumer(CertificateStorageManager consumer) {
        String prop = String.format(CONFIG_TEMPLATE, consumer.name());
        return config.getValue(prop, Boolean.class);
    }

}