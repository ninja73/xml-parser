package com.parser.model;

public class Result {
    private String type;
    private Offer offer;

    public Result(String type, Offer offer) {
        this.type = type;
        this.offer = offer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
