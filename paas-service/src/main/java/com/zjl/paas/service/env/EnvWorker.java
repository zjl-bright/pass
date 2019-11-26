package com.zjl.paas.service.env;

import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import me.zjl.boot.annotation.WorkerMapping;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;

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
    public void init(JsonObject jsonObject) {
        envService.batchInsert(new ArrayList(), res -> {
            System.out.println(res.getInsertedCount());
        });
    }
}
