package org.jugvale.certificate.generator.content;

import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.jugvale.certificate.generator.content.listener.CertificateStorageListener;
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
    Instance<CertificateStorageListener> storageConsumers;
    
    public void generateCertificateContentSyncObserver(@Observes NewCertificateEvent newCertificateEvent) {
        generateCertificateContent(newCertificateEvent);
    }
    
    public void generateCertificateContentAsyncObserver(@ObservesAsync  NewCertificateEvent newCertificateEvent) {
        generateCertificateContent(newCertificateEvent);
    }

    private void generateCertificateContent(NewCertificateEvent newCertificateEvent) {
        Certificate certificate = newCertificateEvent.getCertificate();
        CertificateContent certificateStorage = contentGenerator.generate(certificate);
        storageConsumers.stream().filter(this::filterConsumer).forEach(c -> c.newCertificateContent(certificateStorage));
    }
    
    public void removeCertificateContentSync(@Observes DeletedCertificateEvent deletedCertificateEvent) {
        removeCertificateContent(deletedCertificateEvent);
    }
    
    public void removeCertificateContentAsync(@ObservesAsync DeletedCertificateEvent deletedCertificateEvent) {
        removeCertificateContent(deletedCertificateEvent);
    }

    private void removeCertificateContent(DeletedCertificateEvent deletedCertificateEvent) {
        Certificate certificate = deletedCertificateEvent.getCertificate();
        storageConsumers.stream().filter(this::filterConsumer).forEach(c -> c.removedCertificateContent(certificate));
    }
    
    public boolean filterConsumer(CertificateStorageListener consumer) {
        String prop = String.format(CONFIG_TEMPLATE, consumer.name());
        return config.getValue(prop, Boolean.class);
    }

}