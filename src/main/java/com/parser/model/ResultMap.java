package com.parser.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultMap {
    private Map<Long, Result> resultMap = new ConcurrentHashMap<>();

    public Map<Long, Result> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<Long, Result> resultMap) {
        this.resultMap = resultMap;
    }
}
