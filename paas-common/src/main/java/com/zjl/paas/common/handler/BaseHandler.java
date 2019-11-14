package com.zjl.paas.common.handler;

import com.zjl.paas.common.dao.MongoDao;
import com.zjl.paas.common.model.BaseEntity;
import com.zjl.paas.common.model.Response;
import io.vertx.ext.web.RoutingContext;
import me.zjl.boot.mongodb.MongoRepository;

import javax.inject.Inject;
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

    @Inject
    private MongoRepository mongoRepository;

    public BaseHandler(){
        Class<T> theClass = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        String className = theClass.getName();
        if(className.lastIndexOf(".") > -1){
            collection = className.substring(className.lastIndexOf("."));
        }else{
            collection = className;
        }
    }

    public void save(RoutingContext context){
        mongoRepository.save(collection, context.get("json"), res -> {
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
        mongoRepository.remove(collection, context.get("json"), res -> {
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
        mongoRepository.findOne(collection, context.get("json"), res -> {
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
        mongoRepository.find(collection, context.get("json"), res -> {
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
}
