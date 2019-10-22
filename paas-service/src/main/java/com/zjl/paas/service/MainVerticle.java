package com.zjl.paas.service;

import com.zjl.paas.service.web.WebVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    System.setProperty("vertx.logger-delegate-factory-class-name",
      "io.vertx.core.logging.SLF4JLogDelegateFactory");

    Future.<JsonObject>future(promise -> {
      ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions()
        .addStore(new ConfigStoreOptions().setType("file").setConfig(new JsonObject().put("path", "config.json"))));
      retriever.getConfig(res -> {
        if (res.succeeded()) {
          promise.complete(res.result());
        } else {
          startPromise.fail(res.cause());
        }
      });
    })
      .compose(v -> {
        return Future.future(promise -> {
          vertx.deployVerticle(WebVerticle.class.getName(), new DeploymentOptions().setConfig(v), web -> {
            if (web.succeeded()) {
              log.info(" WebVerticle 启动完毕！");
              startPromise.complete();
            } else {
              startPromise.fail(web.cause());
            }
          });
        });
      });
  }

  public static void main(String... args) {
    Vertx v = Vertx.vertx();
    v.deployVerticle(MainVerticle.class.getName());
  }
}
