//package com.zjl.paas.service.handler;
//
//import io.vertx.core.Vertx;
//import io.vertx.ext.web.RoutingContext;
//
//import java.time.Instant;
//
///**
// * TODO
// *
// * @Auther: zjl
// * @Date: 2019-10-28
// * @Version: 1.0
// */
//public class PostHandler extends BaseService {
//
//    public PostHandler(Vertx vertx) {
//        super(vertx);
//    }
//
//    public static PostHandler create(final Vertx vertx) {
//        return new PostHandler(vertx);
//    }
//
//    @Override
//    public void handle(RoutingContext event) {
//        System.out.println("PostHandler called");
//        final String method = ResponseUtil.getCookieValue(event, Configuration.COOKIE_METHOD);
//        final String traceId = ResponseUtil.getHeaderValue(event, Configuration.TRACE_ID);
//        final long totalTimeTaken = DateUtil.dateDiff(Instant.now(), Long.parseLong(ResponseUtil.getCookieValue(event, Configuration.COOKIE_DATE)));
//        LOGGER.info(String.join(" ", "TraceID [", traceId, "] : Finished executing method ", method, "and took", totalTimeTaken + "", "MS"));
//        event.response().setChunked(true);
//        event.response().end(event.getBodyAsString());
//    }
//}
