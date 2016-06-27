package com.parser;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class BaseActor extends AbstractActor {
    protected LoggingAdapter log = Logging.getLogger(getContext().system(), this);
}
