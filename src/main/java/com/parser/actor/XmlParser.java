package com.parser.actor;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.parser.model.Offer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public abstract class XmlParser extends AbstractActor {

    public LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    protected String fileName;
    protected Map<Long, Offer> offers = new HashMap<>();

    public XmlParser(String fileName) {
        this.fileName = fileName;
        this.offers = getOffers();

        receive(ReceiveBuilder
                .matchAny(this::process)
                .build());
    }

    abstract void process(Object message);

    private Map<Long, Offer> getOffers() {
        Map<Long, Offer> offers = new HashMap<>();
        XMLInputFactory factory = XMLInputFactory.newFactory();

        try {
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(fileName));
            JAXBContext context = JAXBContext.newInstance(Offer.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            while (reader.hasNext()) {
                int event = reader.next();
                if(event == XMLStreamReader.START_ELEMENT && reader.getLocalName().equals("offer")) {
                    JAXBElement<Offer> offerJAXBElement = unmarshaller.unmarshal(reader, Offer.class);
                    Offer offer = offerJAXBElement.getValue();
                    offers.put(offer.getId(), offer);
                }
            }

        } catch (XMLStreamException | FileNotFoundException | JAXBException e) {
            e.printStackTrace();
        }
        return offers;
    }
}
