package com.zjl.paas.service;

import com.zjl.paas.common.model.BaseEntity;
import io.vertx.core.AsyncResult;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClientDeleteResult;
import io.vertx.ext.web.RoutingContext;
import me.zjl.boot.mongodb.MongoRepository;

import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.util.List;
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
            receive(context, res);
        });
    }

    public void save(RoutingContext context, JsonObject jsonObject, Consumer<String> handler){
        mongoRepository.save(collection, jsonObject, res -> {
            receive(context, res, handler);
        });
    }

    //更新文档
    public void updates(RoutingContext context, JsonObject query, JsonObject document){
        mongoRepository.updates(collection, query, document, res -> {
            receive(context, res);
        });
    }

    public void remove(RoutingContext context, JsonObject jsonObject){
        mongoRepository.remove(collection, jsonObject, res -> {
            receive(context, res);
        });
    }

    public void remove(RoutingContext context, JsonObject jsonObject, Consumer<MongoClientDeleteResult> handler){
        mongoRepository.remove(collection, jsonObject, res -> {
            receive(context, res, handler);
        });
    }

    public void findOne(RoutingContext context, JsonObject jsonObject){
        mongoRepository.findOne(collection, jsonObject, res -> {
            receive(context, res);
        });
    }

    public void findOne(RoutingContext context, JsonObject jsonObject, Consumer<JsonObject> handler){
        mongoRepository.findOne(collection, jsonObject, res -> {
            receive(context, res, handler);
        });
    }

    public void findOne(RoutingContext context, JsonObject jsonObject, Function<JsonObject, Boolean> handler){
        mongoRepository.findOne(collection, jsonObject, res -> {
            receive(context, res, handler);
        });
    }

    public void find(RoutingContext context, JsonObject jsonObject){
        mongoRepository.find(collection, jsonObject, res -> {
            receive(context, res);
        });
    }

    public void find(RoutingContext context, JsonObject jsonObject, Function<List<JsonObject>, Boolean> handler){
        mongoRepository.find(collection, jsonObject, res -> {
            receive(context, res, handler);
        });
    }

    //只排序, 针对小型数据集
    public void findWithSort(RoutingContext context, JsonObject query, JsonObject sort){
        mongoRepository.findWithSort(collection, query, sort, res -> {
            receive(context, res);
        });
    }

    //排序且分页, 针对小型数据集
    public void findWithSortAndPage(RoutingContext context, JsonObject query, JsonObject sort, Integer pageSize, Integer pageNum){
        if (pageNum <= 0) {
            pageNum = 1;
        }
        int skip = pageSize * (pageNum - 1);
        mongoRepository.findWithSortAndPage(collection, query, sort, skip, pageSize, res -> {
            receive(context, res);
        });
    }

    //计量
    public void count(RoutingContext context, JsonObject jsonObject){
        mongoRepository.count(collection, jsonObject, res -> {
            receive(context, res);
        });
    }

    //替换文档
    public void replace(RoutingContext context, JsonObject query, JsonObject replace){
        mongoRepository.replace(collection, query, replace, res -> {
            receive(context, res);
        });
    }

    private void receive(RoutingContext context, AsyncResult res){
        try{
            if(res.succeeded()){
                context.put("res", res.result());
                context.next();
            }else{
                context.fail(res.cause());
            }
        } catch (Exception e){
            context.fail(e);
        }
    }

    private void receive(RoutingContext context, AsyncResult res, Consumer handler){
        try{
            if(res.succeeded()){
                handler.accept(res.result());
                context.put("res", res.result());
                context.next();
            }else{
                context.fail(res.cause());
            }
        } catch (Exception e){
            context.fail(e);
        }
    }

    private void receive(RoutingContext context, AsyncResult res, Function handler){
        try{
            if(res.succeeded()){
                context.put("res", handler.apply(res.result()));
                context.next();
            }else{
                context.fail(res.cause());
            }
        } catch (Exception e){
            context.fail(e);
        }
    }

    public MongoRepository getMongoRepository(){
        return mongoRepository;
    }

    public String getCollection(){
        return collection;
    }
}
