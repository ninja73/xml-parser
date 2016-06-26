package com.parser.router;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.parser.model.Offer;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;


public class WorkerThread extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorSelection oldXmlFileParser;
    private static Client client = ClientBuilder.newClient();

    @Override
    public void preStart() throws Exception {
        this.oldXmlFileParser = this.getContext().actorSelection("/user/old-xml-parser");
    }

    public WorkerThread() {
        receive(ReceiveBuilder
                .match(Offer.class, this::sendOffer)
                .match(String.class, str -> oldXmlFileParser.tell(str, self()))
                .matchAny(o -> log.info("received unknown message"))
                .build());
    }

    private void sendOffer(Offer offer) {
        String imgStatus = checkImg(offer.getPicture());
        offer.setPictureStatus(imgStatus);
        oldXmlFileParser.tell(offer, self());
    }

    private String checkImg(String url){
        try {
            if(url == null || url.equals("")) {
                return "p";
            }
            client.property(ClientProperties.CONNECT_TIMEOUT, 100);
            client.property(ClientProperties.READ_TIMEOUT, 100);

            WebTarget webResult = client.target(url);
            Response response = webResult.request().get();
            if(response.getStatus() != 200) {
                throw new ProcessingException("Failed : HTTP error code : " + response.getStatus());
            }
            return "";
        } catch (ProcessingException e) {
            return "p";
        }
    }
}
