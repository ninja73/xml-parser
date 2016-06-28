package com.parser.router;

import akka.actor.*;
import akka.japi.pf.ReceiveBuilder;
import com.parser.BaseActor;
import com.parser.model.ResultMap;
import com.parser.model.Result;

public class Router extends BaseActor {


    private ActorSelection oldXml;
    private ResultMap offerMap = new ResultMap();

    @Override
    public void preStart() throws Exception {
        oldXml = getContext().actorSelection("/user/old-xml-parser");

    }

    public Router() {
        receive(ReceiveBuilder
                .match(Result.class, result -> offerMap.getResultMap().put(result.getId(), result))
                .matchEquals("done", s -> oldXml.tell(offerMap, self()))
                .matchAny(o -> log.info("received unknown message"))
                .build());
    }

}
