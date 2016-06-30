package com.parser.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.SmallestMailboxPool;
import com.parser.BaseActor;
import com.parser.model.Offer;
import com.parser.model.ResultMap;
import com.parser.model.Result;

import java.util.HashMap;
import java.util.Map;

public class CompareXML extends BaseActor {

    Map<Long, Result> resultMap = new HashMap<>();

    private String fileName;

    private ActorRef worker;

    public static Props props(String fileName) {
        return Props.create(CompareXML.class, () -> new CompareXML(fileName));
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        worker = getContext().actorOf(
                new SmallestMailboxPool(20).props(Props.create(WorkerThread.class)),
                "router-pool");
    }

    public CompareXML(String fileName) {
        this.fileName = fileName;

        receive(ReceiveBuilder
                .match(ResultMap.class, this::process)
                .build());
    }
    private void compare(Offer offer) {
        Result result = new Result();
        result.setId(offer.getId());
        result.setPicture(offer.getPicture());
        if(resultMap.containsKey(offer.getId())) {
            Result resultFind = resultMap.get(offer.getId());
            if(resultFind.getOfferHashCode().equals(offer.hashCode())) {
                result.setType("m");
            } else {
                result.setType("");
            }
            resultMap.remove(offer.getId());
        } else {
            result.setType("r");
        }
        worker.tell(result, self());
    }

    private void process(ResultMap map) {
        this.resultMap = map.getResultMap();

        XmlParser<Offer> xmlParser = new XmlParser<>(fileName);
        xmlParser.parse(Offer.class, this::compare, () -> {
            resultMap
                    .entrySet()
                    .parallelStream()
                    .forEach(resultEntry -> worker.tell(resultEntry.getValue(), self()));
            //worker.tell(Kill.getInstance(), self());
        });
    }
}
