package com.parser.model;

public class Result {
    private Long id;
    private String type;
    private String picture;
    private Integer offerHashCode;

    public Result() {
    }

    public Result(Long id, String type, String picture, Integer offerHashCode) {
        this.id = id;
        this.type = type;
        this.picture = picture;
        this.offerHashCode = offerHashCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getOfferHashCode() {
        return offerHashCode;
    }

    public void setOfferHashCode(Integer offerHashCode) {
        this.offerHashCode = offerHashCode;
    }
}
