package com.parser.actor;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.parser.BaseActor;
import com.parser.model.Offer;
import com.parser.model.Result;

public class OldXmlLoader extends BaseActor {

    private String fileName;
    private ActorSelection workerRouter;

    public static Props props(String fileName) {
        return Props.create(OldXmlLoader.class, () -> new OldXmlLoader(fileName));
    }

    @Override
    public void preStart() throws Exception {
        workerRouter = getContext().actorSelection("/user/router");
    }

    public OldXmlLoader(String fileName) {
        this.fileName = fileName;

        receive(ReceiveBuilder
                .match(String.class, s -> parse())
                .build());
    }
    void parse() {
        XmlParser<Offer> xmlParser = new XmlParser<>(fileName);
        xmlParser.parse(Offer.class, (offer) -> {
            Result result = new Result(offer.getId(), "n", offer.getPicture(), offer.hashCode());
            workerRouter.tell(result, self());
        }, () -> workerRouter.tell("done", self()));
    }

}
