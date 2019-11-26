package com.zjl.paas.service.env;

import io.vertx.core.json.JsonArray;
import lombok.extern.slf4j.Slf4j;
import me.zjl.boot.annotation.WorkerMapping;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-11-21
 * @Version: 1.0
 */
@WorkerMapping("env")
@Singleton
@Slf4j
public class EnvWorker {

    @Inject
    private EnvService envService;

    @WorkerMapping("init")
    public void init(JsonArray jsonArray) {
        envService.batchInsert(jsonArray, res -> {
            System.out.println(res.getInsertedCount());
        });
    }
}
