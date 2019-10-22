package com.zjl.paas.service.router;

import io.vertx.core.Vertx;

public class BaseRouter {

  private Vertx vertx;

  public BaseRouter(Vertx vertx){
    this.vertx = vertx;
  }
}
