package com.zjl.paas.service.part;

import com.google.common.base.Throwables;
import com.zjl.paas.service.BaseService;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
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
public class PartService extends BaseService<Part> {

    public void findOne(JsonObject query, Consumer<JsonObject> consumer){
        getMongoRepository().findOne(getCollection(), query, res -> {
            try{
                if(res.succeeded()){
                    consumer.accept(res.result());
                }else{
                    log.error("PartService findone failed , cause by : {}", Throwables.getStackTraceAsString(res.cause()));
                }
            } catch (Exception e){
                log.error("PartService findone failed , cause by : {}", Throwables.getStackTraceAsString(e));
            }
        });
    }
}
