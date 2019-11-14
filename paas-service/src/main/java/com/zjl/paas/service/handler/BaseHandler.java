package com.zjl.paas.service.handler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-10-28
 * @Version: 1.0
 */
public abstract class BaseHandler implements Handler<RoutingContext> {
    protected static Logger log = LoggerFactory.getLogger(BaseHandler.class);
    protected Vertx vertx;

    public BaseHandler(final Vertx vertx) {
        this.vertx = vertx;
    }
}
