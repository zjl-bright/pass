package com.zjl.paas.service;

import com.zjl.paas.common.model.BaseEntity;
import com.zjl.paas.common.model.Response;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import me.zjl.boot.mongodb.MongoRepository;

import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-10-24
 * @Version: 1.0
 */
public class BaseService<T extends BaseEntity> {

    private String collection;

    @Inject
    private MongoRepository mongoRepository;

    public BaseService(){
        Class<T> theClass = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        String className = theClass.getName().toLowerCase();
        if(className.lastIndexOf(".") > -1){
            collection = className.substring(className.lastIndexOf(".")+1);
        }else{
            collection = className;
        }
    }

    public void save(RoutingContext context, JsonObject jsonObject){
        mongoRepository.save(collection, jsonObject, res -> {
            try{
                if(res.succeeded()){
                    context.response().end(Response.ok(res.result()).encodePrettily());
                }else{
                    context.fail(res.cause());
                }
            } catch (Exception e){
                context.fail(e);
            }
        });
    }

    public void save(RoutingContext context, JsonObject jsonObject, Consumer<String> handler){
        mongoRepository.save(collection, jsonObject, res -> {
            try{
                if(res.succeeded()){
                    handler.accept(res.result());
                    context.response().end(Response.ok(res.result()).encodePrettily());
                }else{
                    context.fail(res.cause());
                }
            } catch (Exception e){
                context.fail(e);
            }
        });
    }

    public void remove(RoutingContext context, JsonObject jsonObject){
        mongoRepository.remove(collection, jsonObject, res -> {
            try{
                if(res.succeeded()){
                    context.response().end(Response.ok("true").encodePrettily());
                }else{
                    context.fail(res.cause());
                }
            } catch (Exception e){
                context.fail(e);
            }
        });
    }

    public void findOne(RoutingContext context, JsonObject jsonObject){
        mongoRepository.findOne(collection, jsonObject, res -> {
            try{
                if(res.succeeded()){
                    context.response().end(Response.ok(res.result()).encodePrettily());
                }else{
                    context.fail(res.cause());
                }
            } catch (Exception e){
                context.fail(e);
            }
        });
    }


    public void findOne(RoutingContext context, JsonObject jsonObject, Consumer<JsonObject> handler){
        mongoRepository.findOne(collection, jsonObject, res -> {
            try{
                if(res.succeeded()){
                    handler.accept(res.result());
                    context.response().end(Response.ok().encodePrettily());
                }else{
                    context.fail(res.cause());
                }
            } catch (Exception e){
                context.fail(e);
            }
        });
    }

    public void findOne(RoutingContext context, JsonObject jsonObject, Function<JsonObject, Boolean> handler){
        mongoRepository.findOne(collection, jsonObject, res -> {
            try{
                if(res.succeeded()){
                    context.response().end(Response.ok(handler.apply(res.result())).encodePrettily());
                }else{
                    context.fail(res.cause());
                }
            } catch (Exception e){
                context.fail(e);
            }
        });
    }

    public void find(RoutingContext context, JsonObject jsonObject){
        mongoRepository.find(collection, jsonObject, res -> {
            try{
                if(res.succeeded()){
                    context.response().end(Response.ok(res.result()).encodePrettily());
                }else{
                    context.fail(res.cause());
                }
            } catch (Exception e){
                context.fail(e);
            }
        });
    }

    //只排序, 针对小型数据集
    public void findWithSort(RoutingContext context){
        mongoRepository.findWithSort(collection, context.get("json"), context.get("sort"), res -> {
            try{
                if(res.succeeded()){
                    context.response().end(Response.ok(res.result()).encodePrettily());
                }else{
                    context.fail(res.cause());
                }
            } catch (Exception e){
                context.fail(e);
            }
        });
    }

    //排序且分页, 针对小型数据集
    public void findWithSortAndPage(RoutingContext context){
        Integer pageSize = context.get("pageSize");
        Integer pageNum = context.get("pageNum");
        if (pageNum <= 0) {
            pageNum = 1;
        }
        int skip = pageSize * (pageNum - 1);
        mongoRepository.findWithSortAndPage(collection, context.get("json"), context.get("sort"), skip, pageSize, res -> {
            try{
                if(res.succeeded()){
                    context.response().end(Response.ok(res.result()).encodePrettily());
                }else{
                    context.fail(res.cause());
                }
            } catch (Exception e){
                context.fail(e);
            }
        });
    }

    //计量
    public void count(RoutingContext context){
        mongoRepository.count(collection, context.get("json"), res -> {
            try{
                if(res.succeeded()){
                    context.response().end(Response.ok(res.result()).encodePrettily());
                }else{
                    context.fail(res.cause());
                }
            } catch (Exception e){
                context.fail(e);
            }
        });
    }

    //更新文档
    public void updates(RoutingContext context){
        mongoRepository.updates(collection, context.get("json"), context.get("document"), res -> {
            try{
                if(res.succeeded()){
                    context.response().end(Response.ok(res.result()).encodePrettily());
                }else{
                    context.fail(res.cause());
                }
            } catch (Exception e){
                context.fail(e);
            }
        });
    }

    //替换文档
    public void replace(RoutingContext context){
        mongoRepository.replace(collection, context.get("json"), context.get("document"), res -> {
            try{
                if(res.succeeded()){
                    context.response().end(Response.ok(res.result()).encodePrettily());
                }else{
                    context.fail(res.cause());
                }
            } catch (Exception e){
                context.fail(e);
            }
        });
    }

    public MongoRepository getMongoRepository(){
        return mongoRepository;
    }

    public String getCollection(){
        return collection;
    }
}
