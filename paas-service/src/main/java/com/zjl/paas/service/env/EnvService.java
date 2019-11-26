package com.zjl.paas.service.env;

import com.google.common.base.Throwables;
import com.zjl.paas.service.BaseService;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.mongo.MongoClientBulkWriteResult;
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
public class EnvService extends BaseService<Env> {

    public void batchInsert(JsonArray jsonArray, Consumer<MongoClientBulkWriteResult> handler){
        getMongoRepository().insertAll(getCollection(), jsonArray, res -> {
            try{
                if(res.succeeded()){
                    handler.accept(res.result());
                }else{
                    log.error("Env batchInsert failed , cause by : {}", Throwables.getStackTraceAsString(res.cause()));
                }
            } catch (Exception e){
                log.error("Env batchInsert failed , cause by : {}", Throwables.getStackTraceAsString(e));
            }
        });
    }
}
