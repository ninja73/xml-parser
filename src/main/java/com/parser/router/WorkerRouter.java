package com.parser.router;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.RoundRobinPool;
import com.parser.model.Offer;

public class WorkerRouter extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private ActorRef worker;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        worker = getContext().actorOf(
                new RoundRobinPool(20).props(Props.create(WorkerThread.class)),
                "router-pool");
    }

    public WorkerRouter() {
        receive(ReceiveBuilder
                .match(Offer.class, offer -> worker.tell(offer, self()))
                .matchAny(o -> log.info("received unknown message"))
                .build());
    }
}
