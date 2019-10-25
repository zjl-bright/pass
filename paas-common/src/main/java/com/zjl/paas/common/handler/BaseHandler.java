package com.zjl.paas.common.handler;

import com.zjl.paas.common.dao.MongoDao;
import com.zjl.paas.common.model.BaseEntity;
import com.zjl.paas.common.model.Response;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.ParameterizedType;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-10-24
 * @Version: 1.0
 */
public class BaseHandler<T extends BaseEntity> {

    private String collection;

    private final MongoDao mongoDao;

    public BaseHandler(MongoDao mongoDao){
        this.mongoDao = mongoDao;
        Class<T> theClass = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        collection = theClass.getName();
    }

    public void save(RoutingContext context){
        mongoDao.save(collection, context.get("json"), res -> {
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

    public void remove(RoutingContext context){
        mongoDao.remove(collection, context.get("json"), res -> {
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

    public void findOne(RoutingContext context){
        mongoDao.findOne(collection, context.get("json"), res -> {
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

    public void find(RoutingContext context){
        mongoDao.find(collection, context.get("json"), res -> {
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
        mongoDao.findWithSort(collection, context.get("json"), context.get("sort"), res -> {
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
        mongoDao.findWithSortAndPage(collection, context.get("json"), context.get("sort"), skip, pageSize, res -> {
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
        mongoDao.count(collection, context.get("json"), res -> {
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
        mongoDao.updates(collection, context.get("json"), context.get("document"), res -> {
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
        mongoDao.replace(collection, context.get("json"), context.get("document"), res -> {
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
}
