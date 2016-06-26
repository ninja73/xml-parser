package com.parser.actor;

import akka.actor.Props;
import com.parser.model.Offer;

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

public class OldXmlFileParser extends XmlParser {

    private final Character REMOVE = 'r';
    private final Character MODIFIED = 'm';
    private final Character NEW = 'n';
    private Map<Long, Offer> offers = new HashMap<>();

    public static Props props(String fileName) {
        return Props.create(OldXmlFileParser.class, () -> new OldXmlFileParser(fileName));
    }

    public OldXmlFileParser(String fileName) {
        super(fileName);
        getOffers();
    }

    @Override
    void process(Offer offer) {
        if(offer != null) {
            if (offers.containsKey(offer.getId())) {
                Offer findOffer = offers.get(offer.getId());
                if(!offer.equals(findOffer)) {
                    log.info(String.format("%d %c %s", offer.getId(), MODIFIED, offer.getPictureStatus()));
                }
                offers.remove(offer.getId());
            } else {
                log.info(String.format("%d %c", offer.getId(), NEW));
            }
        }
    }
    @Override
    void process(String message) {
        if(message.equals("done")) {
            offers.entrySet()
                    .stream()
                    .forEach(offer -> log.info(String.format("%d %c", offer.getKey(), REMOVE)));
        }
    }
    private void getOffers() {
        XMLInputFactory factory = XMLInputFactory.newFactory();

        try {
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(fileName));
            Unmarshaller unmarshaller = getUnmarshaller();

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
    }
}
