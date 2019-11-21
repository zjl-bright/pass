package com.zjl.paas.service.work;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
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
@WorkerMapping("project")
@Singleton
public class Code {

    @Inject
    private Vertx vertx;

    @WorkerMapping("clone")
    public void test(JsonObject jsonObject){

    }
}
