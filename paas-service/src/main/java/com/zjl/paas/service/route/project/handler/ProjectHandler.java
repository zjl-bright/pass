package com.zjl.paas.service.route.project.handler;

import com.zjl.paas.common.model.Response;
import com.zjl.paas.service.route.project.ProjectService;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import me.zjl.boot.annotation.RequestMapping;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * TODO
 *
 * @Auther: zhaojl@hshbao.com
 * @Date: 2019-09-21
 * @Version: 1.0
 */
@RequestMapping("/project")
@Singleton
public class ProjectHandler{

  @Inject
  private ProjectService projectService;

  @RequestMapping()
  public void findAll(RoutingContext context){
    projectService.find(context, new JsonObject());
  }

  @RequestMapping(method = HttpMethod.POST)
  public void saveProject(RoutingContext context, JsonObject jsonObject){
    projectService.save(context, jsonObject);
  }

  @RequestMapping(method = HttpMethod.DELETE)
  public void delete(RoutingContext context, JsonObject jsonObject){
    String id = jsonObject.getString("id");
    if(StringUtils.isBlank(id)){
      context.response().end(Response.ok("删除id不可为空").encodePrettily());
      return;
    }
    projectService.remove(context, jsonObject);
  }

  @RequestMapping("/clone/:projectId")
  public void cloneCode(RoutingContext context, String projectId){

    projectService.findOne(context, new JsonObject(projectId), res ->{

      return  true;
    });
  }
}
