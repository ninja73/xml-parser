package com.parser.router;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.RoundRobinPool;
import com.parser.model.Offer;
import com.parser.model.Result;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkerRouter extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private ActorRef worker;
    private Map<Long, Result> offerMap = new ConcurrentHashMap<>();
    private AtomicInteger done = new AtomicInteger(0);

    @Override
    public void preStart() throws Exception {
        worker = getContext().actorOf(
                new RoundRobinPool(20).props(Props.create(WorkerThread.class)),
                "router-pool");
    }

    public WorkerRouter() {
        receive(ReceiveBuilder
                .match(Offer.class, this::process)
                .match(String.class, this::done)
                .matchAny(o -> log.info("received unknown message"))
                .build());
    }

    public void done(String string) {
        int count = done.incrementAndGet();
        log.info("MAP: " + offerMap.size());
        if(count >= 2) {
            offerMap.entrySet().stream().forEach(o -> {
                if(!o.getValue().getType().isEmpty()){
                    worker.tell(o.getValue(), self());
                }
            });
        }
    }

    public void process(Offer offer) {
        String type = "";
        if(offerMap.containsKey(offer.getId())) {
            Result offer1 = offerMap.get(offer.getId());
            if(offer.hashCode() != offer1.getOfferHashCode()) {
                type = "m";
            }
            worker.tell(new Result(offer.getId(), type, offer.getPicture(), offer.hashCode()), self());
            offerMap.remove(offer.getId());
        } else {
            switch (sender().path().name()) {
                case "new-xml-parser":
                    type = "n";
                    break;
                case "old-xml-parser":
                    type = "r";
                    break;
            }
            offerMap.put(offer.getId(),
                    new Result(offer.getId(),
                            type,
                            offer.getPicture(),
                            offer.hashCode()));
        }
    }
}
