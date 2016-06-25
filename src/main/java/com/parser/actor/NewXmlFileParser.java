package com.parser.actor;

import akka.actor.Props;
import akka.actor.Terminated;
import com.parser.model.Offer;

public class NewXmlFileParser extends XmlParser {

    public static Props props(String fileName) {
        return Props.create(NewXmlFileParser.class, () -> new NewXmlFileParser(fileName));
    }

    public NewXmlFileParser(String fileName) {
        super(fileName);
    }

    @Override
    void process(Object message) {
        if(message instanceof Offer) {
            Offer offer = (Offer) message;
            if (offers.containsKey(offer.getId())) {
                Offer findOffer = offers.get(offer.getId());
                if(offer.equals(findOffer)) {
                    log.info("EQUALS");
                } else {
                    log.info("NOT EQUALS");
                }
            } else {
                //log.info("NOT FOUND OFFER: " + offer.getId());
            }
        }
        if(offers.size() < 1) {
           // sender().tell(Terminated("close"));
        }
    }
}
