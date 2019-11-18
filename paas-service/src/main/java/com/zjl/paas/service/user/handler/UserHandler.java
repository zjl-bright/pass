package com.zjl.paas.service.user.handler;

import com.google.common.base.Strings;
import com.zjl.paas.service.user.service.UserService;
import com.zjl.paas.service.util.EncryptUtil;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import me.zjl.boot.annotation.RequestMapping;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

/**
 * TODO
 *
 * @Auther: zhaojl@hshbao.com
 * @Date: 2019-09-21
 * @Version: 1.0
 */
@RequestMapping("/user")
@Singleton
public class UserHandler{

  @Inject
  private UserService userService;

  @RequestMapping(value = "/login", method = HttpMethod.POST)
  public void login(RoutingContext ctx, JsonObject jsonObject){
    String mobile = jsonObject.getString("mobile");
    String password = jsonObject.getString("password");
    if(Strings.isNullOrEmpty(mobile)){
      ctx.response().end("用户名不可为空");
      return;
    }
    if(Strings.isNullOrEmpty(password)){
      ctx.response().end("密码不可为空");
      return;
    }
    jsonObject.remove("password");

    userService.findOne(ctx, jsonObject, res -> {
      if(Objects.nonNull(res)){
        return EncryptUtil.match(password, res.getString("password"));
      }
      return false;
    });
  }

//  public void register(RoutingContext context){
//    JsonObject user = context.get("user");
//    mongoDao.insert(collection, user, res -> {
//      try{
//        if(res.succeeded()){
//          //其他可能发生异常的业务代码
//          context.response().end(Response.ok(res.result()).encodePrettily());
//        }else{
//          context.fail(res.cause());
//        }
//      } catch (Exception e){
//        context.fail(e);
//      }
//    });
//  }
//
//  public void getOne(RoutingContext context){
//    mongoDao.findOne(collection, new JsonObject().put("", ""), res -> {
//      try{
//          if(res.succeeded()){
//            //其他可能发生异常的业务代码
//            context.response().end(Response.ok(res.result()).encodePrettily());
//          }else{
//            context.fail(res.cause());
//          }
//      } catch (Exception e){
//        context.fail(e);
//      }
//    });
//  }
//
//  public void getAll(RoutingContext context){
//    mongoDao.find(collection, new JsonObject(), res -> {
//      try{
//        if(res.succeeded()){
//          //其他可能发生异常的业务代码
//          context.response().end(Response.ok(res.result()).encodePrettily());
//        }else{
//          context.fail(res.cause());
//        }
//      } catch (Exception e){
//        context.fail(e);
//      }
//    });
//  }
//
//  public void getBath(RoutingContext context){
//    mongoDao.findBath(collection, new JsonObject(), res -> {
//      context.response().end(res.encodePrettily());
//    });
//  }
}
