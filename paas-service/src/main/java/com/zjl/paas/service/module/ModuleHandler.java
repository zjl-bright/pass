package com.zjl.paas.service.module;

import com.zjl.paas.common.model.Response;
import io.vertx.core.MultiMap;
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
    public void find(RoutingContext context, MultiMap map){
        String partId = map.get("partId");
        if(StringUtils.isBlank(partId)){
            context.response().end(Response.ok("分部id不可为空").encodePrettily());
            return;
        }
        moduleService.find(context, new JsonObject().put("partId", partId));
    }

    @RequestMapping(method = HttpMethod.DELETE)
    public void delete(RoutingContext context, MultiMap map){
        String id = map.get("id");
        if(StringUtils.isBlank(id)){
            context.response().end(Response.ok("删除id不可为空").encodePrettily());
            return;
        }
        moduleService.remove(context, new JsonObject().put("_id", id));
    }

    @RequestMapping(method = HttpMethod.POST)
    public void save(RoutingContext context, JsonObject jsonObject){
        String partId = jsonObject.getString("partId");
        String name = jsonObject.getString("name");
        String targetPath = jsonObject.getString("targetPath");
        String dirPath = jsonObject.getString("dirPath");
        String cmd = jsonObject.getString("cmd");

        if(StringUtils.isBlank(partId)){
            context.response().end(Response.ok("partId不可为空").encodePrettily());
            return;
        }
        if(StringUtils.isBlank(name)){
            context.response().end(Response.ok("name不可为空").encodePrettily());
            return;
        }
        if(StringUtils.isBlank(targetPath)){
            context.response().end(Response.ok("targetPath不可为空").encodePrettily());
            return;
        }
        if(StringUtils.isBlank(dirPath)){
            context.response().end(Response.ok("dirPath不可为空").encodePrettily());
            return;
        }
        if(StringUtils.isBlank(cmd)){
            context.response().end(Response.ok("cmd不可为空").encodePrettily());
            return;
        }
        moduleService.save(context, jsonObject);
    }

    @RequestMapping("/reset/:moudleId")
    public void reset(RoutingContext context, String projectId){
//        projectService.findOne(context, new JsonObject().put("projectId", projectId), res ->{
//            vertx.eventBus().send("project.clone", res);
//        });
    }
}
