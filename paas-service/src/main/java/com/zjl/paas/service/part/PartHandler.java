package com.zjl.paas.service.part;

import com.zjl.paas.common.model.Response;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
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
@RequestMapping("/part")
@Singleton
public class PartHandler {

    @Inject
    private PartService partService;

    @Inject
    private Vertx vertx;

    @RequestMapping()
    public void find(RoutingContext context, MultiMap map){
        String projectId = map.get("projectId");
        if(StringUtils.isBlank(projectId)){
            context.response().end(Response.ok("项目id不可为空").encodePrettily());
            return;
        }
        partService.find(context, new JsonObject().put("projectId", projectId));
    }

    @RequestMapping(method = HttpMethod.DELETE)
    public void delete(RoutingContext context, MultiMap map){
        String id = map.get("id");
        if(StringUtils.isBlank(id)){
            context.response().end(Response.ok("id不可为空").encodePrettily());
            return;
        }
        partService.remove(context, new JsonObject().put("_id", id));
    }

    @RequestMapping(method = HttpMethod.POST)
    public void save(RoutingContext context, JsonObject jsonObject){
        String projectId = jsonObject.getString("projectId");
        String name = jsonObject.getString("name");
        String gitPath = jsonObject.getString("gitPath");

        if(StringUtils.isBlank(projectId)){
            context.response().end(Response.ok("projectId不可为空").encodePrettily());
            return;
        }
        if(StringUtils.isBlank(name)){
            context.response().end(Response.ok("name不可为空").encodePrettily());
            return;
        }
        if(StringUtils.isBlank(gitPath)){
            context.response().end(Response.ok("gitPath不可为空").encodePrettily());
            return;
        }

        partService.save(context, jsonObject, res -> {
            vertx.eventBus().send("part.clone", jsonObject);
        });
    }

    @RequestMapping("/reset/:moudleId")
    public void reset(RoutingContext context, String projectId){
//        projectService.findOne(context, new JsonObject().put("projectId", projectId), res ->{
//            vertx.eventBus().send("project.clone", res);
//        });
    }
}
