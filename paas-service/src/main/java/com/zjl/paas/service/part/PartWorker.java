package com.zjl.paas.service.part;

import com.google.common.base.Throwables;
import com.zjl.paas.service.project.ProjectService;
import com.zjl.paas.service.thread.CommandWaitForThread;
import com.zjl.paas.service.util.JGitUtil;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import me.zjl.boot.annotation.WorkerMapping;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.util.Objects;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-11-21
 * @Version: 1.0
 */
@WorkerMapping("part")
@Singleton
@Slf4j
public class PartWorker {

    @Inject
    private ProjectService projectService;

    @Inject
    private JGitUtil jGit;

    @Inject
    @Named("package_sh")
    private String sh;

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
        String cmd = jsonObject.getString("cmd");

        if(jGit.checkoutAndPull(path, branchName)){
            log.info("part：{}, 本地目录：{}, checkout and pull 成功", jsonObject.getString("name"), path);
            doPackage(path, cmd);
        }else{
            log.error("part：{}, 本地目录：{}, checkout and pull 失败", jsonObject.getString("name"), path);
        }
    }

    private void doPackage(String path, String cmd){
        call("cd" + path + "&&" + cmd);
        call("cd" + path + "&&" + cmd);

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

    private void callScript(String script, String[] args) throws Exception{
        try {
            //启动独立线程等待process执行完成
            CommandWaitForThread commandThread = new CommandWaitForThread("sh " + script, args);
            commandThread.start();

            while (!commandThread.isFinish()) {
                log.info("shell " + script + "还未执行完毕,10s后重新探测");
                Thread.sleep(10000);
            }

            //检查脚本执行结果状态码
            if(commandThread.getExitValue() != 0){
                throw new RuntimeException("shell " + script + "执行失败,exitValue = " + commandThread.getExitValue());
            }
            log.info("shell " + script + "执行成功,exitValue = " + commandThread.getExitValue());
        }
        catch (Exception e){
            throw new Exception("执行脚本发生异常,脚本路径" + script, e);
        }
    }

}
