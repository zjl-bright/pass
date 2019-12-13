package com.zjl.paas.service.project;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.zjl.paas.service.module.ModuleService;
import com.zjl.paas.service.part.PartService;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import me.zjl.boot.annotation.RequestMapping;
import me.zjl.boot.utils.ResponseUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * TODO
 *
 * @Auther: zhaojl@hshbao.com
 * @Date: 2019-09-21
 * @Version: 1.0
 */
@Slf4j
@Singleton
@RequestMapping("/project")
public class ProjectHandler{

    @Inject
    private Vertx vertx;

    @Inject
    private ProjectService projectService;

    @Inject
    private PartService partService;

    @Inject
    private ModuleService moduleService;

    @RequestMapping()
    public void find(RoutingContext context){
        projectService.findWithEnd(context, new JsonObject());
    }

    @RequestMapping(value = "/:id", method = HttpMethod.DELETE)
    public void delete(RoutingContext context, String id){
        if(ResponseUtil.endIfParamBlank(context, id, "id不可为空")){
            return;
        }

        Future.future(promise ->{
            projectService.remove(context, new JsonObject().put("_id", id), res -> {promise.complete();});
        }).compose(res -> Future.<List<JsonObject>>future(promise -> {
                    JsonObject dePart = new JsonObject().put("projectId", id);
                    partService.find(context, dePart, partRes -> {promise.complete(partRes);});
        })).compose(res -> Future.<List<JsonObject>>future(promise -> {
                    JsonObject dePart = new JsonObject().put("projectId", id);
                    partService.remove(context, dePart, partDeleteRes -> {promise.complete(res);});
        })).compose(res -> Future.future(promise -> {

            List<Future> list = Lists.newArrayList();
            res.forEach(part -> {
                list.add(Future.<Void>future(promise1 -> {
                    String path = part.getString("path");
                    vertx.fileSystem().deleteRecursive(path, true, ar -> {
                        if(ar.failed()){
                            log.error("delete project: {}，delete file path: {}", id, path, Throwables.getStackTraceAsString(ar.cause()));
                        }
                        promise1.complete();
                    });
                }));
            });
            CompositeFuture.join(list).setHandler(ar -> {
                if(ar.failed()){
                    throw new RuntimeException(ar.cause());
                }
                promise.complete();
            });
        })).compose(res -> Future.future(promise -> {
            moduleService.removeWithEnd(context, new JsonObject().put("partId", id));
        }));
//        projectService.remove(context, new JsonObject().put("_id", id), projectRes -> {
//            if(projectRes.getRemovedCount() > 0){
//                JsonObject dePart = new JsonObject().put("projectId", id);
//                partService.find(context, dePart, partRes -> {
//                    partService.remove(context, dePart, partDeleteRes -> {
//                        if(partDeleteRes.getRemovedCount() > 0){
//                            List<Future> list = Lists.newArrayList();
//                            partRes.forEach(part -> {
//                                list.add(Future.<Void>future(promise -> {
//                                    String path = part.getString("path");
//                                    vertx.fileSystem().deleteRecursive(path, true, ar -> {
//                                        if(ar.failed()){
//                                            log.error("delete project: {}，delete file path: {}", id, path, Throwables.getStackTraceAsString(ar.cause()));
//                                        }
//                                        promise.complete();
//                                    });
//                                }));
//
//                            });
//                            CompositeFuture.all(list).setHandler(ar -> {
//                                moduleService.remove(context, new JsonObject().put("partId", id));
//                            })
//
//
//                        }
//                    });
//                });
//            }
//        });
    }

    @RequestMapping(method = HttpMethod.POST)
    public void save(RoutingContext context, JsonObject jsonObject){
        String dir = jsonObject.getString("dir");
        if(ResponseUtil.endIfParamBlank(context, dir, "dir不可为空")){
            return;
        }else{
            String temp = dir.replace("/", "");
            if(ResponseUtil.endIfExpressionTrue(context, temp.length()==0, "dir不可没有目录")){
                return;
            }
        }
        String name = jsonObject.getString("name");
        if(ResponseUtil.endIfParamBlank(context, name, "name不可为空")){
            return;
        }

        String path;
        if(dir.startsWith("/")){
            path = System.getProperty("user.dir") + dir;
        }else{
            path = System.getProperty("user.dir") + "/" + dir;
        }
        if(path.endsWith("/")){
            path = path.substring(0, path.lastIndexOf("/"));
        }
        jsonObject.put("path", path);

        projectService.saveWithEnd(context, jsonObject);
    }
}
