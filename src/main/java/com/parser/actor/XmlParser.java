package com.parser.actor;

import akka.actor.ActorSelection;
import akka.japi.pf.ReceiveBuilder;
import com.parser.BaseActor;
import com.parser.model.Offer;
import com.parser.model.ResultMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class XmlParser extends BaseActor {

    protected ActorSelection workerRouter;

    private String fileName;

    @Override
    public void preStart() throws Exception {
        workerRouter = getContext().actorSelection("/user/router");
    }

    public XmlParser(String fileName) {
        this.fileName = fileName;

        receive(ReceiveBuilder
                .match(String.class, s -> parse())
                .match(ResultMap.class, this::compareXml)
                .build());
    }

    private Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Offer.class);
        return context.createUnmarshaller();
    }

    abstract void process(Offer offer);
    abstract void compareXml(ResultMap map);
    abstract void finish();

    protected void parse() {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        XMLStreamReader reader = null;
        try {
            reader = factory.createXMLStreamReader(new FileInputStream(fileName));
            Unmarshaller unmarshaller = getUnmarshaller();
            while (reader.hasNext()) {
                int event = reader.next();
                if(event == XMLStreamReader.START_ELEMENT && reader.getLocalName().equals("offer")) {
                    JAXBElement<Offer> offerJAXBElement = unmarshaller.unmarshal(reader, Offer.class);
                    Offer offer = offerJAXBElement.getValue();
                    process(offer);
                } else if(event == XMLStreamReader.END_DOCUMENT) {
                    finish();
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
