package com.zjl.paas.service.part;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.zjl.paas.service.module.ModuleHandler;
import com.zjl.paas.service.module.ModuleService;
import com.zjl.paas.service.module.ModuleWorker;
import com.zjl.paas.service.util.JGitUtil;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import me.zjl.boot.annotation.WorkerMapping;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-11-21
 * @Version: 1.0
 */
@Slf4j
@Singleton
@WorkerMapping("part")
public class PartWorker {

    public static Boolean flag = true;
    @Inject
    private JGitUtil jGit;

    @Inject
    @Named("package_sh")
    private String sh;

    @Inject
    private ModuleService moduleService;

    @Inject
    private ModuleWorker moduleWorker;

    @WorkerMapping("clone")
    public void clone(JsonObject jsonObject){
        String gitPath = jsonObject.getString("gitPath");
        String path = jsonObject.getString("path");

        File file = new File(path);
        if(file.exists()){
            if(!deleteFile(file.listFiles())){
                log.error("part：{}, gitpath：{}, clone到本地目录：{}, 删除旧文件失败", jsonObject.getString("name"), gitPath, path);
                return;
            }
        }else{
            if(!file.mkdirs()){
                log.error("part：{}, gitpath：{}, clone到本地目录：{}, 创建新文件夹失败", jsonObject.getString("name"), gitPath, path);
                return;
            }
        }
        if(jGit.cloneRepository(gitPath, file.getAbsolutePath())){
            log.info("part：{}, gitpath：{}, clone到本地目录：{}, 成功", jsonObject.getString("name"), gitPath, path);
        }else{
            log.error("part：{}, gitpath：{}, clone到本地目录：{}, 失败", jsonObject.getString("name"), gitPath, path);
        }
    }

    private Boolean deleteFile(File[] files){
        if(Objects.nonNull(files)){
            int length = files.length;
            for(int index = 0; index < length; index++){
                if(files[index].isDirectory()){
                    if(!deleteFile(files[index].listFiles())){
                        return false;
                    }

                    if(!files[index].delete()){
                        return false;
                    }
                }else{
                    if(!files[index].delete()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @WorkerMapping("cmdPackage")
    public void cmdPackage(JsonObject jsonObject){
        String branchName = jsonObject.getString("branchName");
        String path = jsonObject.getString("path");
        String packageCmd = jsonObject.getString("cmd");
        Boolean isDeploy = jsonObject.getBoolean("isDeploy");
        String ip = jsonObject.getString("ip");

        moduleService.find(new JsonObject().put("partId", jsonObject.getString("_id")), res -> {
            List<String> modulePaths = Lists.newArrayList();
            List<String> targetPaths = Lists.newArrayList();
            List<String> dockerFilePaths = Lists.newArrayList();
            res.forEach(module -> {
                modulePaths.add(module.getString("modulePath"));
                targetPaths.add(module.getString("targetPath"));
                dockerFilePaths.add(module.getString("dockerFilePath"));
            });

            moduleWorker.start(path, branchName, path, packageCmd, modulePaths, targetPaths, dockerFilePaths,
                    isDeploy, ip);
        });
    }

}
