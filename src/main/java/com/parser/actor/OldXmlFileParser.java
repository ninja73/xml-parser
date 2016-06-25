package com.parser.actor;

import akka.actor.ActorRef;
import akka.actor.Props;

public class OldXmlFileParser extends XmlParser {

    private ActorRef workerRouter;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        workerRouter = this.getContext().actorFor("/user/router");
    }
    public static Props props(String fileName) {
        return Props.create(OldXmlFileParser.class, () -> new OldXmlFileParser(fileName));
    }

    public OldXmlFileParser(String fileName) {
        super(fileName);
    }

    @Override
    void process(Object message) {
        this.offers.entrySet().stream().forEach(p -> workerRouter.tell(p.getValue(), self()));
    }
}
