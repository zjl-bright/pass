package com.zjl.paas.service.project;

import com.google.common.base.Throwables;
import com.zjl.paas.common.model.Response;
import com.zjl.paas.service.BaseService;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.function.Consumer;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-11-18
 * @Version: 1.0
 */
@Slf4j
@Singleton
public class ProjectService extends BaseService<Project> {


    public void findOne(JsonObject jsonObject, Consumer<JsonObject> handler){
        getMongoRepository().findOne(getCollection(), jsonObject, res -> {
            try{
                if(res.succeeded()){
                    handler.accept(res.result());
                }else{
                    log.error("Project findOne failed , cause by : {}", Throwables.getStackTraceAsString(res.cause()));
                }
            } catch (Exception e){
                log.error("Project findOne failed , cause by : {}", Throwables.getStackTraceAsString(e));
            }
        });
    }
}
