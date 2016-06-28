package com.parser.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import com.parser.model.Offer;
import com.parser.model.ResultMap;
import com.parser.model.Result;

import java.util.HashMap;
import java.util.Map;

public class CompareXml extends XmlParser {

    private Map<Long, Result> resultMap = new HashMap<>();
    private ActorRef worker;

    public static Props props(String fileName) {
        return Props.create(CompareXml.class, () -> new CompareXml(fileName));
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        worker = getContext().actorOf(
                new RoundRobinPool(20).props(Props.create(WorkerThread.class)),
                "router-pool");
    }

    public CompareXml(String fileName) {
        super(fileName);
    }

    @Override
    void process(Offer offer) {
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

    @Override
    void compareXml(ResultMap map) {
        this.resultMap = map.getResultMap();
        parse();
    }

    @Override
    void finish() {
        resultMap.entrySet().stream().forEach(resultEntry -> worker.tell(resultEntry.getValue(), self()));
    }
}
