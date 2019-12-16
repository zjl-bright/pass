package com.zjl.paas.service.module;

import com.zjl.paas.service.part.PartService;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import me.zjl.boot.annotation.RequestMapping;
import me.zjl.boot.utils.ResponseUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-11-25
 * @Version: 1.0
 */
@Singleton
@RequestMapping("/module")
public class ModuleHandler {

    @Inject
    private ModuleService moduleService;

    @Inject
    private PartService partService;

    @Inject
    private Vertx vertx;

    @RequestMapping(value = "/:partId")
    public void find(RoutingContext context, String partId){
        if(ResponseUtil.endIfParamBlank(context, partId, "分部id不可为空")){
            return;
        }
        moduleService.findWithEnd(context, new JsonObject().put("partId", partId));
    }

    @RequestMapping(value = "/:id", method = HttpMethod.DELETE)
    public void delete(RoutingContext context, String id){
        if(ResponseUtil.endIfParamBlank(context, id, "id不可为空")){
            return;
        }
        moduleService.removeWithEnd(context, new JsonObject().put("_id", id));
    }

    @RequestMapping(method = HttpMethod.POST)
    public void save(RoutingContext context, JsonObject jsonObject){
        String partId = jsonObject.getString("partId");
        if(ResponseUtil.endIfParamBlank(context, partId, "分部Id不可为空")){
            return;
        }
        String name = jsonObject.getString("name");
        if(ResponseUtil.endIfParamBlank(context, name, "名称不可为空")){
            return;
        }
        String target = jsonObject.getString("target");
        if(ResponseUtil.endIfParamBlank(context, target, "路径不可为空")){
            return;
        }
        String cmd = jsonObject.getString("cmd");
        if(ResponseUtil.endIfParamBlank(context, cmd, "打包命令不可为空")){
            return;
        }

        partService.findOne(context, new JsonObject().put("_id", partId), res -> {
            String path = res.getString("path");
            String targetPath;
            if(target.startsWith("/")){
                targetPath = path + target;
            }else{
                targetPath = path + "/" + target;
            }
            String modulePath = targetPath.substring(0, targetPath.lastIndexOf("/"));
            String dockerFilePath = modulePath + "/Dockerfile";
            moduleService.saveWithEnd(context, jsonObject.put("targetPath", targetPath)
                    .put("modulePath", modulePath).put("dockerFilePath", dockerFilePath));
        });
    }

    @RequestMapping("/package")
    public void cmdpackage(RoutingContext context, MultiMap map){
        String moudleId = map.get("_id");
        if(ResponseUtil.endIfParamBlank(context, moudleId, "id不可为空")){
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

        moduleService.findOneWithEnd(context, new JsonObject().put("_id", moudleId), res ->{
            vertx.eventBus().send("module.cmdpackage", res.put("branchName", branchName).put("ip", ip));
        });
    }
}
