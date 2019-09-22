package org.jugvale.certificate.generator.content.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.fop.svg.PDFTranscoder;
import org.jugvale.certificate.generator.content.CertificateContentGenerator;
import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateContent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@ApplicationScoped
public class SVGPDFCertificateContentGenerator implements CertificateContentGenerator {


    private String parser;
    private SAXSVGDocumentFactory factory;
    
    @PostConstruct
    void init() {
        parser = XMLResourceDescriptor.getXMLParserClassName();
        factory = new SAXSVGDocumentFactory(parser);
    }

    @Override
    public CertificateContent generate(Certificate certificate) {
        String attendeeNameField = certificate.certificateModel.attendeeNameField;
        String certificateKeyField = certificate.certificateModel.certificateKeyField;
        
        String certificateContent = certificate.certificateModel.content;

        Document doc = buildDocument(certificateContent);
        Element attendeNameEl = doc.getElementById(attendeeNameField);
        Element certificateKeyEl = doc.getElementById(certificateKeyField);
        
        attendeNameEl.setTextContent(certificate.registration.attendee.name);
        certificateKeyEl.setTextContent(certificate.generationKey);
        
        CertificateContent storage = new CertificateContent();
        storage.certificate = certificate;
        storage.content = DOMUtilities.getXML(doc);
        storage.contentBin = generatePDF(doc);
        
        return storage;
    }

    private byte[] generatePDF(Document doc) {
        TranscoderInput inputSVG = new TranscoderInput(doc);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TranscoderOutput outputPdf = new TranscoderOutput(baos);
        Transcoder transcoder = new PDFTranscoder();
        try {
            transcoder.transcode(inputSVG, outputPdf);
            return baos.toByteArray();
        } catch (TranscoderException e) {
            throw new RuntimeException("Error transforming SVG to PDF.", e);
        }
    }

    private Document buildDocument(String certificateContent) {
        try {
            return factory.createDocument("", new StringReader(certificateContent));
        } catch (IOException e) {
            throw new RuntimeException("Error loading document content", e);
        }
    }

    
}
