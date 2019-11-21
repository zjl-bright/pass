package com.zjl.paas.service.route.project.handler;

import io.vertx.ext.web.RoutingContext;
import me.zjl.boot.annotation.RequestMapping;

import javax.inject.Singleton;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-11-18
 * @Version: 1.0
 */
@RequestMapping("/project")
@Singleton
public class CMDHandler {


    @RequestMapping()
    public void findAll(RoutingContext context){

    }
}
