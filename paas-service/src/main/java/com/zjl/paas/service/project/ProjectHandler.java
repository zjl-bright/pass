package com.zjl.paas.service.project;

import com.zjl.paas.common.enums.Env;
import com.zjl.paas.common.model.Response;
import com.zjl.paas.service.env.EnvHandler;
import com.zjl.paas.service.env.EnvService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import me.zjl.boot.annotation.RequestMapping;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
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
    private Vertx vertx;

    @Inject
    private EnvHandler envHandler;

    @Inject
    private ProjectService projectService;

    @Inject
    private EnvService envService;

    @Named("project_dir_path")
    private String projectPath;

    @RequestMapping()
    public void find(RoutingContext context){
        projectService.find(context, new JsonObject());
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

    @RequestMapping(method = HttpMethod.POST)
    public void save(RoutingContext context, JsonObject jsonObject){
        String dirPath = projectPath + "/" + jsonObject.getString("dirName");
        jsonObject.put("dirPath", dirPath);
        projectService.save(context, jsonObject, res -> {
            Env[] envs = Env.values();
            int length = envs.length;
            JsonArray jsonArray = new JsonArray();
            for(int i = 0; i < length; i++){
                jsonArray.add(new JsonObject()
                        .put("projectId", res)
                        .put("name", envs[i].env())
                        .put("branchName", envs[i].branch()));
            }
            vertx.eventBus().send("env.init", jsonArray);
        });
    }

    @RequestMapping(method = HttpMethod.PUT)
    public void update(RoutingContext context, JsonObject jsonObject){
        String dirPath = projectPath + "/" + jsonObject.getString("dirName");
        jsonObject.put("dirPath", dirPath);
        projectService.save(context, jsonObject);
    }
}
