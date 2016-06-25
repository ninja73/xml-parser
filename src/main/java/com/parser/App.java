package com.parser;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.parser.actor.NewXmlFileParser;
import com.parser.actor.OldXmlFileParser;
import com.parser.router.WorkerRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    public static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String... args) {
        if(args.length > 1) {
            ActorSystem actorSystem = ActorSystem.create("actor-system");

            String oldXml = args[0];
            String newXml = args[1];

            ActorRef start = actorSystem.actorOf(OldXmlFileParser.props(oldXml), "old-xml-parser");
            actorSystem.actorOf(NewXmlFileParser.props(newXml), "new-xml-parser");

            actorSystem.actorOf(Props.create(WorkerRouter.class), "router");
            start.tell("run", ActorRef.noSender());
        } else {
            log.error("set argument old and new xml file");
        }
    }

}
