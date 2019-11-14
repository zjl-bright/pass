package com.zjl.paas.service.handler;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-10-28
 * @Version: 1.0
 */
public class PreHandler extends BaseHandler {

    public PreHandler(Vertx vertx) {
        super(vertx);
    }

    public static PreHandler create(final Vertx vertx) {
        return new PreHandler(vertx);
    }

    @Override
    public void handle(RoutingContext event) {
        //can be used as AOP
        System.out.println("PreHandler called");
        event.next();
    }
}
