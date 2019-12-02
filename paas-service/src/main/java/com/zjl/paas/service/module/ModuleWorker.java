package com.zjl.paas.service.module;

import com.google.common.base.Throwables;
import com.zjl.paas.service.part.PartService;
import com.zjl.paas.service.thread.CommandWaitForThread;
import com.zjl.paas.service.util.JGitUtil;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import me.zjl.boot.annotation.WorkerMapping;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-11-21
 * @Version: 1.0
 */
@WorkerMapping("module")
@Singleton
@Slf4j
public class ModuleWorker {

    @Inject
    private PartService partService;

    @Inject
    private JGitUtil jGit;

    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @WorkerMapping("cmdPackage")
    public Boolean cmdPackage(JsonObject jsonObject){
        String branchName = jsonObject.getString("branchName");
        String partId = jsonObject.getString("partId");
        String modulePath = jsonObject.getString("modulePath");
        String targetPath = jsonObject.getString("targetPath");
        String cmd = jsonObject.getString("cmd");

        partService.findOne(new JsonObject().put("_id", partId), part -> {
            String partPath = part.getString("path");
            start(partPath, branchName, modulePath, cmd, targetPath);
        });
        return true;
    }

    public void start(String partPath, String branchName, String modulePath, String cmd, String targetPath){
        if(jGit.checkoutAndPull(partPath, branchName)){
            log.info("目录：{}, checkout and pull 成功", partPath);

            doPackage(modulePath, cmd);
            String tag = tag(branchName);
            doPackage(targetPath, "docker build -t git.hshbao.com:5000/" + tag + " -f" + modulePath + "/Dockerfile .");
            doPackage("docker push git.hshbao.com:5000/" + tag);
        }else{
            log.error("目录：{}, checkout and pull 失败", partPath);
        }
    }

    private String tag(String branchName){
        LocalDateTime localDateTime = LocalDateTime.now();
        return branchName + "_" + df.format(localDateTime);
    }

    private void doPackage(String path, String cmd){
        call("cd " + path + " && " + cmd);
    }

    private void doPackage(String cmd){
        call(cmd);
    }

    private void call(String cmd){
        try {
            //启动独立线程等待process执行完成
            CommandWaitForThread commandThread = new CommandWaitForThread(cmd);
            commandThread.start();

            while (!commandThread.isFinish()) {
                log.info("命令：{}, 还未执行完毕,10s后重新探测", cmd);
                Thread.sleep(10000);
            }

            //检查脚本执行结果状态码
            if(commandThread.getExitValue() != 0){
                log.error("命令：{}, 执行失败, exitValue = {}", cmd, commandThread.getExitValue());
                return;
            }
            log.info("命令：{}, 执行成功, exitValue = {}", cmd, commandThread.getExitValue());
        }catch (Exception e){
            log.error("命令：{}, 执行失败, cause by : ", cmd, Throwables.getStackTraceAsString(e));
            return;
        }
    }
}
