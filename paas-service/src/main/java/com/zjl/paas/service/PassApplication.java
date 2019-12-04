package com.zjl.paas.service;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.extern.slf4j.Slf4j;
import me.zjl.boot.VertxBoot;

import java.util.concurrent.TimeUnit;

@Slf4j
public class PassApplication {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMaxWorkerExecuteTime(TimeUnit.MINUTES.toNanos(20)));
    VertxBoot.run(vertx, PassApplication.class, args);
  }
}
