package com.zjl.paas.service.project;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import me.zjl.boot.annotation.RequestMapping;
import me.zjl.boot.utils.ResponseUtil;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Objects;

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
    public void find(RoutingContext context){
        projectService.find(context, new JsonObject());
    }

    //TODO 是否要删除该项目的所有配置，比如已经下拉的代码（实际的文件）还有对应的 part和module
    @RequestMapping(value = "/:id", method = HttpMethod.DELETE)
    public void delete(RoutingContext context, String id){
        if(ResponseUtil.endIfParamBlank(context, id, "id不可为空")){
            return;
        }
        projectService.remove(context, new JsonObject().put("_id", id));
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
        JsonArray types = jsonObject.getJsonArray("types");
        if(Objects.nonNull(types)){
            if(!types.contains("生产")){
                types.add("生产");
            }
        }else{
            jsonObject.put("types", new JsonArray().add("生产"));
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

        projectService.save(context, jsonObject);
    }
}
