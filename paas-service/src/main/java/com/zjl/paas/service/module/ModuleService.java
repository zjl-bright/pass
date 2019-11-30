package com.zjl.paas.service.module;

import com.google.common.base.Throwables;
import com.zjl.paas.service.BaseService;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.List;
import java.util.function.Consumer;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-11-25
 * @Version: 1.0
 */
@Slf4j
@Singleton
public class ModuleService extends BaseService<Module> {

    public void find(JsonObject jsonObject, Consumer<List<JsonObject>>consumer){
        getMongoRepository().find(getCollection(), jsonObject, res -> {
            try{
                if(res.succeeded()){
                    consumer.accept(res.result());
                }else{
                    log.error("Module find failed , cause by : {}", Throwables.getStackTraceAsString(res.cause()));
                }
            } catch (Exception e){
                log.error("Module find failed , cause by : {}", Throwables.getStackTraceAsString(e));
            }
        });
    }
}
