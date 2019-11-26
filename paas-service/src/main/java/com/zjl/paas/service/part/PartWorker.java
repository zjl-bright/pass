package com.zjl.paas.service.part;

import com.zjl.paas.service.project.ProjectService;
import com.zjl.paas.service.util.JGitUtil;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import me.zjl.boot.annotation.WorkerMapping;

import javax.inject.Inject;
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

    @WorkerMapping("clone")
    public void clone(JsonObject jsonObject){
        String gitPath = jsonObject.getString("gitPath");
        String projectId = jsonObject.getString("projectId");

        projectService.findOne(new JsonObject().put("_id", projectId), res -> {
            String filePath = res.getString("filePath");
            File file = new File(filePath);
            if(file.exists()){
                deleteFile(file.listFiles());
            }else{
                file.mkdirs();
            }
            jGit.cloneRepository(gitPath, filePath);
        });
    }

    private void deleteFile(File[] files){
        if(Objects.nonNull(files)){
            int length = files.length;
            for(int index = 0; index < length; index++){
                if(files[index].isDirectory()){
                    deleteFile(files[index].listFiles());
                    files[index].delete();
                }else{
                    files[index].delete();
                }
            }
        }
    }
}


//if(gitPath.indexOf("/") > -1 && gitPath.indexOf(".") > -1) {
//        String projectName = gitPath.substring(gitPath.lastIndexOf("/") + 1, gitPath.lastIndexOf("."));
//        String filePath = file_path + "/" + jsonObject.getString("name") + "/" + projectName;
//        jsonObject.put("filePath", filePath);
//        }else{
//        throw new RuntimeException("");
//        }
