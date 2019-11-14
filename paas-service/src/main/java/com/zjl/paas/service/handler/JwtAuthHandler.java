package com.zjl.paas.service.handler;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-10-10
 * @Version: 1.0
 */
public class JwtAuthHandler {

  private final Vertx vertx;

  private JWTAuth provider;

  public JwtAuthHandler(Vertx vertx){
    this.vertx = vertx;
    provider = JWTAuth.create(vertx, new JWTAuthOptions()
      .addPubSecKey(new PubSecKeyOptions()
        .setAlgorithm("HS256")
        .setPublicKey("keyboard cat")
        .setSymmetric(true)));
  }

  public String createToken(String username, String password){
    return provider.generateToken(new JsonObject());
  }


}
