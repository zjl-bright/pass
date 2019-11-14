package com.zjl.paas.service.project.handler;

import com.zjl.paas.common.handler.BaseHandler;
import com.zjl.paas.common.model.Response;
import com.zjl.paas.service.project.entity.Project;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import me.zjl.boot.annotation.RequestMapping;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Singleton;
import java.util.Map;

/**
 * TODO
 *
 * @Auther: zhaojl@hshbao.com
 * @Date: 2019-09-21
 * @Version: 1.0
 */
@RequestMapping("/api/project")
@Singleton
public class ProjectHandler extends BaseHandler<Project> {

  @RequestMapping(path = "/:aa/:bb", method = HttpMethod.POST)
  public void find(String aa, String bb, MultiMap map, RoutingContext context, JsonObject jsonObject){
    map.forEach(entity -> {
            System.out.println(entity.getKey() + "~~" + entity.getValue());
    });
//    String aa = map.get("aa");
//    String bb = map.get("bb");
    String name = map.get("name");
    String gitPath = map.get("gitPath");
    Project project = jsonObject.mapTo(Project.class);
    if(StringUtils.isBlank(name)){
      context.response().end(Response.fail("项目名称不能为空").encodePrettily());
    }
    findWithSortAndPage(context);

////排序且分页, 针对小型数据集
//    public void findWithSortAndPage(RoutingContext context){
//      Integer pageSize = context.get("pageSize");
//      Integer pageNum = context.get("pageNum");
//      if (pageNum <= 0) {
//        pageNum = 1;
//      }
//      int skip = pageSize * (pageNum - 1);
//      mongoRepository.findWithSortAndPage(collection, context.get("json"), context.get("sort"), skip, pageSize, res -> {
//        try{
//          if(res.succeeded()){
//            context.response().end(Response.ok(res.result()).encodePrettily());
//          }else{
//            context.fail(res.cause());
//          }
//        } catch (Exception e){
//          context.fail(e);
//        }
//      });
//    }
  }

  @RequestMapping("/save")
  public void save(RoutingContext context, MultiMap map, JsonObject jsonObject){
    String name = map.get("name");
    String gitPath = map.get("gitPath");

//    Project project = jsonObject.mapTo(Project.class);
//    if(StringUtils.isBlank(name)){
//      context.response().end(Response.fail("项目名称不能为空").encodePrettily());
//    }
    save(context);
  }
}
