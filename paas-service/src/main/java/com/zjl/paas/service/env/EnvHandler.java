package com.zjl.paas.service.env;

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
@RequestMapping("/env")
@Singleton
public class EnvHandler {

    @Inject
    private EnvService envService;

    @RequestMapping()
    public void find(RoutingContext context, MultiMap map){
        String projectId = map.get("projectId");
        if(StringUtils.isBlank(projectId)){
            context.response().end(Response.ok("项目id不可为空").encodePrettily());
            return;
        }
        envService.find(context, new JsonObject().put("projectId", projectId));
    }

    @RequestMapping(method = HttpMethod.DELETE)
    public void delete(RoutingContext context, MultiMap map){
        String id = map.get("id");
        if(StringUtils.isBlank(id)){
            context.response().end(Response.ok("id不可为空").encodePrettily());
            return;
        }
        envService.remove(context, new JsonObject().put("_id", id));
    }

    @RequestMapping(method = HttpMethod.POST)
    public void save(RoutingContext context, JsonObject jsonObject){
        String projectId = jsonObject.getString("projectId");
        String name = jsonObject.getString("name");
        String branchName = jsonObject.getString("branchName");

        if(StringUtils.isBlank(projectId)){
            context.response().end(Response.ok("projectId不可为空").encodePrettily());
            return;
        }
        if(StringUtils.isBlank(name)){
            context.response().end(Response.ok("name不可为空").encodePrettily());
            return;
        }
        if(StringUtils.isBlank(branchName)){
            context.response().end(Response.ok("branchName不可为空").encodePrettily());
            return;
        }
        envService.save(context, jsonObject);
    }
}
