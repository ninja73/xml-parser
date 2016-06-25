package com.parser.router;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.parser.model.Offer;

public class WorkerThread extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef newXmlFileParser;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.newXmlFileParser = this.getContext().actorFor("/user/new-xml-parser");
    }

    public WorkerThread() {
        receive(ReceiveBuilder
                .match(Offer.class, offer -> newXmlFileParser.tell(offer, self()))
                .match(Terminated.class, x -> checkFinished())
                .matchAny(o -> log.info("received unknown message"))
                .build());
    }

    private void checkFinished() {
        if(context().children().isEmpty()) {
            context().stop(self());
        }
    }
}
