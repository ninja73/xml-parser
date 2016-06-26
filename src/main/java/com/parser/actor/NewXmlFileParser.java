package com.parser.actor;

import akka.actor.ActorSelection;
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

public class NewXmlFileParser extends XmlParser {

    private ActorSelection workerRouter;

    @Override
    public void preStart() throws Exception {
        workerRouter = getContext().actorSelection("/user/router");
    }
    public static Props props(String fileName) {
        return Props.create(NewXmlFileParser.class, () -> new NewXmlFileParser(fileName));
    }

    public NewXmlFileParser(String fileName) {
        super(fileName);
    }

    @Override
    void process(Offer offer) {
        log.warning("message not support");
    }

    @Override
    void process(String message) {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        try {
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(fileName));
            Unmarshaller unmarshaller = getUnmarshaller();
            JAXBElement<Offer> offerJAXBElement;

            while (reader.hasNext()) {
                int event = reader.next();
                if(event == XMLStreamReader.START_ELEMENT && reader.getLocalName().equals("offer")) {
                    offerJAXBElement = unmarshaller.unmarshal(reader, Offer.class);
                    workerRouter.tell(offerJAXBElement.getValue(), self());
                }
            }

        } catch (XMLStreamException | FileNotFoundException | JAXBException e) {
            e.printStackTrace();
        }
        workerRouter.tell("done", self());
    }
}
