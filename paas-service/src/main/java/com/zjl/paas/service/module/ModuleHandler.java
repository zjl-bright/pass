package com.zjl.paas.service.module;

import com.zjl.paas.common.model.Response;
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
 * @Auther: zjl
 * @Date: 2019-11-25
 * @Version: 1.0
 */
@RequestMapping("/module")
@Singleton
public class ModuleHandler {

    @Inject
    private ModuleService moduleService;

    @RequestMapping()
    public void find(RoutingContext context){
        moduleService.find(context, new JsonObject());
    }

    @RequestMapping(method = HttpMethod.DELETE)
    public void delete(RoutingContext context, JsonObject jsonObject){
        String id = jsonObject.getString("id");
        if(StringUtils.isBlank(id)){
            context.response().end(Response.ok("删除id不可为空").encodePrettily());
            return;
        }
        moduleService.remove(context, jsonObject);
    }

    @RequestMapping(method = HttpMethod.POST)
    public void save(RoutingContext context, JsonObject jsonObject){
//        String dirPath = codePath + "/" + jsonObject.getString("name");
//        jsonObject.put("dirPath", dirPath);
//        projectService.save(context, jsonObject);
    }

    @RequestMapping("/reset/:moudleId")
    public void reset(RoutingContext context, String projectId){
//        projectService.findOne(context, new JsonObject().put("projectId", projectId), res ->{
//            vertx.eventBus().send("project.clone", res);
//        });
    }
}
