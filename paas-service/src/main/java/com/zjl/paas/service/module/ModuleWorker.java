package com.zjl.paas.service.module;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.zjl.paas.service.part.PartService;
import com.zjl.paas.service.util.JGitUtil;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import me.zjl.boot.annotation.WorkerMapping;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-11-21
 * @Version: 1.0
 */
@Slf4j
@Singleton
@WorkerMapping("module")
public class ModuleWorker {

    @Inject
    private PartService partService;

    @Inject
    private JGitUtil jGit;

    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @WorkerMapping("cmdPackage")
    public Boolean cmdPackage(JsonObject jsonObject){
        String branchName = jsonObject.getString("branchName");
        String modulePath = jsonObject.getString("modulePath");
        String cmd = jsonObject.getString("cmd");
        String targetPath = jsonObject.getString("targetPath");
        String dockerFilePath = jsonObject.getString("dockerFilePath");
        Boolean isDeploy = jsonObject.getBoolean("isDeploy");
        String ip = jsonObject.getString("ip");

        partService.findOne(new JsonObject().put("_id", jsonObject.getString("partId")), part -> {
            String partPath = part.getString("path");
            start(partPath, branchName, modulePath, cmd, Lists.newArrayList(modulePath), Lists.newArrayList(targetPath), Lists.newArrayList(dockerFilePath), isDeploy, ip);
        });
        return true;
    }

    public void start(String partPath, String branchName, String packagePath, String packageCmd, List<String> modulePaths,
                      List<String> targetPaths, List<String> dockerFilePaths, Boolean isDeploy, String ip){
        String tag_suffix = tag(branchName);
        if(!doPackage(partPath, branchName, packagePath, packageCmd)){
            return;
        }
        if(modulePaths.size() == targetPaths.size() && targetPaths.size() == dockerFilePaths.size()){
            int count = modulePaths.size();
            for(int i =0; i < count; i++){
                if(!doDocker(modulePaths.get(i), targetPaths.get(i), dockerFilePaths.get(i), tag_suffix)){
                    return;
                }
            }
        }else{
            log.error("start failed, partPath : {}, branchName : {}, packagePath : {}, packageCmd : {}, modulePaths size : {}," +
                    " targetPaths size : {}, dockerFilePaths size : {}, isDeploy : {}, ip : {}, ", partPath, branchName, packagePath, packageCmd,
                    modulePaths.size(), targetPaths.size(), dockerFilePaths.size(), isDeploy, ip);
            return;
        }
        if(isDeploy){
            doDeploy(tag_suffix, ip);
        }
    }

    private String tag(String branchName){
        LocalDateTime localDateTime = LocalDateTime.now();
        return branchName + "_" + df.format(localDateTime);
    }

    private Boolean doPackage(String partPath, String branchName, String packagePath, String packageCmd){
        log.info("~~~~~~~~~~start checkout and pull~~~~~~~~~~");
        if(jGit.checkoutAndPull(partPath, branchName)){
            log.info("git checkout and pull success, partPath ：{}, branchName : {}", partPath, branchName);
            log.info("~~~~~~~~~~start run package cmd~~~~~~~~~~");
            if(run(packagePath, packageCmd)){
                log.info("package success, packagePath ：{}, packageCmd : {}", packagePath, packageCmd);
            }else{
                log.error("package failed, packagePath : {}, packageCmd : {}", packagePath, packageCmd);
                return false;
            }
        }else{
            log.error("git checkout and pull failed, partPath ：{}, branchName : {}", partPath, branchName);
            return false;
        }
        return true;
    }

    private Boolean doDocker(String modulePath, String targetPath, String dockerfilePath, String tag_suffix){
        log.info("~~~~~~~~~~start docker~~~~~~~~~~");
        String tag = tag(modulePath, tag_suffix);
        if(run(targetPath, "docker build -t " + tag + " -f " + dockerfilePath + " .")){
            log.info("make docker image success, targetPath : {}, cmd : {}", targetPath, "docker build -t " + tag + " -f " + dockerfilePath + " .");
            if(run("docker push " + tag)){
                log.info("docker push success, cmd : {}", "docker push " + tag);
            }else{
                log.error("docker push failed, cmd : {}", "docker push " + tag);
                return false;
            }
        }else{
            log.error("make docker image failed, targetPath : {}, cmd : {}", targetPath, "docker build -t " + tag + " -f " + dockerfilePath + " .");
            return false;
        }
        return true;
    }

    private String tag(String moudule, String tag){
        moudule = moudule.substring(moudule.lastIndexOf("/")+1);
        return "git.hshbao.com:5000/" + moudule + ":" + tag;
    }

    private void doDeploy(String tag, String ip){

        String cmd = "docker run ~~~~~";

    }

    private Boolean run(String cmd){
        return call(cmd);
    }

    private Boolean run(String path, String cmd){
        return call("cd " + path + " && " + cmd);
    }

    private Boolean call(String cmd){
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            //写出脚本执行中的过程信息
            BufferedReader infoInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorInput = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = "";
            while ((line = infoInput.readLine()) != null) {
                log.info(line);
            }
            while ((line = errorInput.readLine()) != null) {
                log.error(line);
            }
            infoInput.close();
            errorInput.close();

            //阻塞执行线程直至脚本执行完成后返回
            process.waitFor();
        } catch (Throwable e) {
            log.error("Process failed, cmd：{}, cause by : {}", cmd, Throwables.getStackTraceAsString(e));
            return false;
        }
        return true;
    }
}
