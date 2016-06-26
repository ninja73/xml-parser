package com.parser.actor;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.parser.model.Offer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public abstract class XmlParser extends AbstractActor {

    protected LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    protected String fileName;

    public XmlParser(String fileName) {
        this.fileName = fileName;

        receive(ReceiveBuilder
                .match(Offer.class, this::process)
                .match(String.class, this::process)
                .build());
    }

    protected Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Offer.class);
        return context.createUnmarshaller();
    }

    abstract void process(String message);

    abstract void process(Offer offer);
}
