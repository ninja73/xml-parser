package com.parser.router;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.parser.model.Result;

import java.io.IOException;
import java.net.*;
import java.util.StringJoiner;


public class WorkerThread extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public WorkerThread() {
        receive(ReceiveBuilder
                .match(Result.class, this::sendOffer)
                .matchAny(o -> log.info("received unknown message"))
                .build());
    }

    private void sendOffer(Result result) {
        Boolean checkImg = checkImg(result.getOffer().getPicture());
        StringJoiner joiner = new StringJoiner(" ");
        if(!result.getType().isEmpty()) {
            joiner.add(result.getType());
        }
        if(checkImg) {
            joiner.add("p");
        }
        if(joiner.length() > 0 ) log.info(result.getOffer().getId() + " " + joiner.toString());
    }

    private Boolean checkImg(String url){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(100);
            connection.setReadTimeout(100);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if(responseCode != 200)
                return false;
            return true;
        } catch (IOException exception) {
            return false;
        }
    }
}
