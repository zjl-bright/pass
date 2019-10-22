package com.zjl.paas.service.user.handler;

import com.zjl.paas.common.dao.MongoDao;
import com.zjl.paas.common.model.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * TODO
 *
 * @Auther: zhaojl@hshbao.com
 * @Date: 2019-09-21
 * @Version: 1.0
 */
public class UserHandler {

  private final String collection = "user";

  private final MongoDao mongoDao;

  public UserHandler(MongoDao mongoDao){
    this.mongoDao = mongoDao;
  }

  public void register(RoutingContext context){
    JsonObject user = context.get("user");
    mongoDao.insert(collection, user, res -> {
      try{
        if(res.succeeded()){
          //其他可能发生异常的业务代码
          context.response().end(Response.ok(res.result()).encodePrettily());
        }else{
          context.fail(res.cause());
        }
      } catch (Exception e){
        context.fail(e);
      }
    });
  }

  public void getOne(RoutingContext context){
    mongoDao.findOne(collection, new JsonObject().put("", ""), res -> {
      try{
          if(res.succeeded()){
            //其他可能发生异常的业务代码
            context.response().end(Response.ok(res.result()).encodePrettily());
          }else{
            context.fail(res.cause());
          }
      } catch (Exception e){
        context.fail(e);
      }
    });
  }

  public void getAll(RoutingContext context){
    mongoDao.find(collection, new JsonObject(), res -> {
      try{
        if(res.succeeded()){
          //其他可能发生异常的业务代码
          context.response().end(Response.ok(res.result()).encodePrettily());
        }else{
          context.fail(res.cause());
        }
      } catch (Exception e){
        context.fail(e);
      }
    });
  }

  public void getBath(RoutingContext context){
    mongoDao.findBath(collection, new JsonObject(), res -> {
      context.response().end(res.encodePrettily());
    });
  }
}
