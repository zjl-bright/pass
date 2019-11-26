package com.zjl.paas.service.module;

import com.zjl.paas.service.util.JGitUtil;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import me.zjl.boot.annotation.RequestMapping;
import me.zjl.boot.annotation.WorkerMapping;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;

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
    private Vertx vertx;

    @Inject
    private JGitUtil jGit;

//    @WorkerMapping("clone")
//    public Boolean clone(JsonObject jsonObject){
//
//        String gitPath = jsonObject.getString("gitPath");
//        String filePath = jsonObject.getString("filePath");
//
//        File file = new File(filePath);
//        if(file.exists()){
//            deleteFile(file);
//        }else{
//            file.mkdirs();
//        }
//        return jGit.cloneRepository(gitPath, filePath);
//    }

//    private void deleteFile(File file){
//        if(file){
//
//        }
//        int length = files.length;
//        for(int index = 0; index < length; index++){
//            if(files[index].isDirectory()){
//                deleteFile(files[index].listFiles());
//                files[index].delete();
//            }else{
//                files[index].delete();
//            }
//        }
//    }
}


//if(gitPath.indexOf("/") > -1 && gitPath.indexOf(".") > -1) {
//        String projectName = gitPath.substring(gitPath.lastIndexOf("/") + 1, gitPath.lastIndexOf("."));
//        String filePath = file_path + "/" + jsonObject.getString("name") + "/" + projectName;
//        jsonObject.put("filePath", filePath);
//        }else{
//        throw new RuntimeException("");
//        }
