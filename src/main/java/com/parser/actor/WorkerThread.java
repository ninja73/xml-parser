package com.parser.actor;

import akka.japi.pf.ReceiveBuilder;
import com.parser.BaseActor;
import com.parser.model.Result;

import java.io.IOException;
import java.net.*;
import java.util.StringJoiner;


public class WorkerThread extends BaseActor {

    public WorkerThread() {
        receive(ReceiveBuilder
                .match(Result.class, this::sendOffer)
                .matchAny(o -> log.info("received unknown message"))
                .build());
    }

    private void sendOffer(Result result) {
        Boolean checkImg = checkImg(result.getPicture());
        if(!result.getType().isEmpty() || checkImg) {
            StringJoiner joiner = new StringJoiner(" ");
            if (!result.getType().isEmpty()) {
                joiner.add(result.getType());
            }
            if (checkImg) {
                joiner.add("p");
            }
            if (joiner.length() > 0) log.info(result.getId() + " " + joiner.toString());
        }
    }

    private Boolean checkImg(String url){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(20);
            connection.setReadTimeout(20);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException exception) {
            return false;
        }
    }
}
