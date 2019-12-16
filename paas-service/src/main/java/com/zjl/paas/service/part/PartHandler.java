package com.zjl.paas.service.part;

import com.google.common.base.Throwables;
import com.zjl.paas.service.module.ModuleService;
import com.zjl.paas.service.project.ProjectService;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import me.zjl.boot.annotation.RequestMapping;
import me.zjl.boot.utils.ResponseUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-11-25
 * @Version: 1.0
 */
@Slf4j
@Singleton
@RequestMapping("/part")
public class PartHandler {

    @Inject
    private ProjectService projectService;

    @Inject
    private PartService partService;

    @Inject
    private ModuleService moduleService;

    @Inject
    private Vertx vertx;

    @RequestMapping(value = "/:projectId")
    public void find(RoutingContext context, String projectId){
        if(ResponseUtil.endIfParamBlank(context, projectId, "projectId不可为空")){
            return;
        }
        Future.<List<JsonObject>>future(promise -> {
            partService.find(context, new JsonObject().put("projectId", projectId), res -> {
                promise.complete(res);
            });
        }).compose(v -> Future.future(promise -> {
            List<Future> list = new ArrayList();
            for (JsonObject part : v) {
                list.add(Future.future(promise1 -> {
                    String partId = part.getString("_id");
                    moduleService.find(context, new JsonObject().put("partId", partId), res -> {
                        part.put("modules", res);
                        promise1.complete();
                    });
                }));
            }
            CompositeFuture.all(list).setHandler(ar -> {
                ResponseUtil.end(context, ar.map(v));
            });
        }));
    }

    @RequestMapping(value = "/:id", method = HttpMethod.DELETE)
    public void delete(RoutingContext context, String id){
        if(ResponseUtil.endIfParamBlank(context, id, "id不可为空")){
            return;
        }
        JsonObject dePart = new JsonObject().put("_id", id);
        Future.<JsonObject>future(promise ->{
            partService.findOne(context, dePart, partRes -> {promise.complete(partRes);});
        }).compose(res -> Future.<JsonObject>future(promise -> {
            partService.remove(context, dePart, partRes -> {promise.complete(res);});
        })).compose(res -> Future.future(promise -> {
            String path = res.getString("path");
            vertx.fileSystem().deleteRecursive(path, true, ar -> {
                if(ar.failed()){
                    log.error("delete part: {}，delete file path: {}", id, path, Throwables.getStackTraceAsString(ar.cause()));
                }
                promise.complete();
            });
        })).compose(res -> Future.future(promise -> {
            moduleService.removeWithEnd(context, new JsonObject().put("partId", id));
        }));
    }


    @RequestMapping(method = HttpMethod.POST)
    public void save(RoutingContext context, JsonObject jsonObject){
        String projectId = jsonObject.getString("projectId");
        if(ResponseUtil.endIfParamBlank(context, projectId, "projectId不可为空")){
            return;
        }
        String name = jsonObject.getString("name");
        if(ResponseUtil.endIfParamBlank(context, name, "name不可为空")){
            return;
        }
        String gitPath = jsonObject.getString("gitPath");
        if(ResponseUtil.endIfParamBlank(context, gitPath, "gitPath不可为空")){
            return;
        }
        if(ResponseUtil.endIfExpressionTrue(context, !gitPath.contains("/")||!gitPath.contains("."), "代码库地址格式不正确") ){
            return;
        }

        String gitName = gitPath.substring(gitPath.lastIndexOf("/"), gitPath.lastIndexOf("."));
        projectService.findOne(context, new JsonObject().put("_id", projectId), project -> {
            String projectPath = project.getString("path");
            jsonObject.put("path", projectPath + gitName);
            partService.saveWithEnd(context, jsonObject, part -> {
                vertx.eventBus().send("part.clone", jsonObject);
            });
        });
    }

    @RequestMapping(method = HttpMethod.PUT)
    public void update(RoutingContext context, JsonObject jsonObject){
        String id = jsonObject.getString("_id");
        if(ResponseUtil.endIfParamBlank(context, id, "_id不可为空")){
            return;
        }

        jsonObject.remove("_id");
        if(jsonObject.isEmpty()){
            ResponseUtil.end(context, "入参不能只有_id");
            return;
        }

        partService.findOne(context, new JsonObject().put("_id", id), part -> {
            part.mergeIn(jsonObject);
            if(jsonObject.containsKey("gitPath")){
                String gitPath = jsonObject.getString("gitPath");
                if(ResponseUtil.endIfParamBlank(context, gitPath, "gitPath不可为空")){
                    return;
                }
                String oldPath = part.getString("path");
                String dir = oldPath.substring(0, oldPath.lastIndexOf("/"));
                String gitName = gitPath.substring(gitPath.lastIndexOf("/"), gitPath.lastIndexOf("."));

                part.put("path", dir + gitName);
            }

            partService.saveWithEnd(context, part);
        });
    }

    @RequestMapping("/clone/:id")
    public void clone(RoutingContext context, String id){
        if(ResponseUtil.endIfParamBlank(context, id, "id不可为空")){
            return;
        }
        partService.findOneWithEnd(context, new JsonObject().put("_id", id), res -> {
            vertx.eventBus().send("part.clone", res);
        });
    }

    @RequestMapping("/package")
    public void cmdPackage(RoutingContext context, MultiMap map){
        String id = map.get("_id");
        if(ResponseUtil.endIfParamBlank(context, id, "id不可为空")){
            return;
        }
        String branchName = map.get("branchName");
        if(ResponseUtil.endIfParamBlank(context, branchName, "分支名不可为空")){
            return;
        }
        String ip = map.get("ip");
        if(ResponseUtil.endIfParamBlank(context, ip, "ip不可为空")){
            return;
        }

        partService.findOneWithEnd(context, new JsonObject().put("_id", id), res -> {
            vertx.eventBus().send("part.cmdPackage", res.put("branchName", branchName).put("ip", ip));
        });
    }
}
