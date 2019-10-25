package com.zjl.paas.service.project.handler;

import com.zjl.paas.common.dao.MongoDao;
import com.zjl.paas.common.handler.BaseHandler;
import com.zjl.paas.common.model.Response;
import com.zjl.paas.service.project.entity.Project;
import io.netty.util.internal.StringUtil;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;

/**
 * TODO
 *
 * @Auther: zhaojl@hshbao.com
 * @Date: 2019-09-21
 * @Version: 1.0
 */
public class ProjectHandler extends BaseHandler<Project> {

  private final String collection = "project";

  private final MongoDao mongoDao;

  public ProjectHandler(MongoDao mongoDao){
    super(mongoDao);
    this.mongoDao = mongoDao;
  }

  @Override
  public void save(RoutingContext context){
    JsonObject jsonObject = context.getBodyAsJson();
    String name = jsonObject.getString("name");
    if(StringUtils.isBlank(name)){
      context.response().end(Response.fail("项目名称不能为空").encodePrettily());
    }
    mongoDao.save(collection, context.get("json"), res -> {
      try{
        if(res.succeeded()){
          context.response().end(Response.ok(res.result()).encodePrettily());
        }else{
          context.fail(res.cause());
        }
      } catch (Exception e){
        context.fail(e);
      }
    });
  }


}
