package com.parser.actor;

import com.parser.model.Offer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class XmlParser<T> {

    public static final Logger log = LoggerFactory.getLogger(XmlParser.class);
    private String fileName;

    public XmlParser(String fileName){
        this.fileName = fileName;
    }

    private Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Offer.class);
        return context.createUnmarshaller();
    }

    public void parse(Class<T> type, MethodParam<T> process, Method finish) {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        XMLStreamReader reader = null;
        try {
            reader = factory.createXMLStreamReader(new FileInputStream(fileName));
            Unmarshaller unmarshaller = getUnmarshaller();
            while (reader.hasNext()) {
                int event = reader.next();
                if(event == XMLStreamReader.START_ELEMENT && reader.getLocalName().equals("offer")) {
                    JAXBElement<T> offerJAXBElement = unmarshaller.unmarshal(reader, type);
                    T offer = offerJAXBElement.getValue();
                    process.apply(offer);
                } else if(event == XMLStreamReader.END_DOCUMENT) {
                    finish.apply();
                }
            }

        } catch (XMLStreamException | FileNotFoundException | JAXBException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (XMLStreamException e) {
                log.error(e.getMessage());
            }
        }
    }
}
