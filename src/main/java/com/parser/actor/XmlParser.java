package com.parser.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;
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

public class XmlParser extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorSelection workerRouter;

    private String fileName;

    @Override
    public void preStart() throws Exception {
        workerRouter = getContext().actorSelection("/user/router");
    }

    public static Props props(String fileName) {
        return Props.create(XmlParser.class, () -> new XmlParser(fileName));
    }

    public XmlParser(String fileName) {
        this.fileName = fileName;

        receive(ReceiveBuilder
                .match(String.class, this::process)
                .build());
    }

    private Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Offer.class);
        return context.createUnmarshaller();
    }

    private void process(String message) {
        log.info("Start read file: " + fileName);
        parse();
    }

    private void parse() {
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
                    workerRouter.tell(offer, self());
                }
            }
            workerRouter.tell("done", self());

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
