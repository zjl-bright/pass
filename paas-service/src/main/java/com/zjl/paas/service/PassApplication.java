package com.zjl.paas.service;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import me.zjl.boot.VertxBoot;

@Slf4j
public class PassApplication {

  public static void main(String[] args) {
    System.out.println(args.length);
    VertxBoot.run(Vertx.vertx(), PassApplication.class, args);
  }
}
