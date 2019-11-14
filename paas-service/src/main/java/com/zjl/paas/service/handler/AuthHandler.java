package com.zjl.paas.service.handler;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-10-28
 * @Version: 1.0
 */
public class AuthHandler extends BaseHandler {

    public AuthHandler(Vertx vertx) {
        super(vertx);
    }

    public static AuthHandler create(final Vertx vertx) {
        return new AuthHandler(vertx);
    }

    @Override
    public void handle(RoutingContext event) {
        System.out.println("AuthHandler called");
        final String authHeader = event.request().headers().get(HttpHeaders.AUTHORIZATION.toString());
//        final SessionHelper helper = new SessionHelper();
//        helper.validate(authHeader, handler -> {
//            if (!handler.failed()) {
//                event.getDelegate().setUser(handler.result());
//                event.next();
//            } else {
//                event.fail(new InvalidTokenException());
//            }
//        });
    }
}
