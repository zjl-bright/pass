package com.zjl.paas.service.env;

import com.google.common.base.Throwables;
import com.zjl.paas.service.BaseService;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClientBulkWriteResult;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public void batchInsert(List<JsonObject> jsonObjects, Consumer<MongoClientBulkWriteResult> handler){
        getMongoRepository().insertAll(getCollection(), jsonObjects, res -> {
            try{
                if(res.succeeded()){
                    handler.accept(res.result());
                }else{
                    log.error("batchInsert failed , cause by : {}", Throwables.getStackTraceAsString(res.cause()));
                }
            } catch (Exception e){
                log.error("batchInsert failed , cause by : {}", Throwables.getStackTraceAsString(e));
            }
        });
    }
}
