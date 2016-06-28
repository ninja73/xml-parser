package com.parser.actor;

import akka.actor.Props;
import com.parser.model.Offer;
import com.parser.model.ResultMap;
import com.parser.model.Result;

public class LoadXml extends XmlParser {

    public static Props props(String fileName) {
        return Props.create(LoadXml.class, () -> new LoadXml(fileName));
    }

    public LoadXml(String fileName) {
        super(fileName);
    }

    @Override
    void process(Offer offer) {
        Result result = new Result(offer.getId(), "n", offer.getPicture(), offer.hashCode());
        workerRouter.tell(result, self());
    }

    @Override
    void compareXml(ResultMap map) {
        log.warning("not support message");
    }

    @Override
    void finish() {
        workerRouter.tell("done", self());
    }
}
