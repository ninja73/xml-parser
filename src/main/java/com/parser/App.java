package com.parser;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.parser.actor.XmlParser;
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

            ActorRef newFile = actorSystem.actorOf(XmlParser.props(newXml), "new-xml-parser");
            ActorRef oldFile = actorSystem.actorOf(XmlParser.props(oldXml), "old-xml-parser");

            actorSystem.actorOf(Props.create(WorkerRouter.class), "router");
            newFile.tell("run", ActorRef.noSender());
            oldFile.tell("run", ActorRef.noSender());
        } else {
            log.error("set argument old and new xml file");
        }
    }

}
