package com.zjl.paas.common.dao;

import com.google.common.base.Throwables;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.*;
import lombok.extern.slf4j.Slf4j;
import org.cool.common.exception.ServiceException;

import java.util.List;

/**
 * TODO
 *
 * @Auther: zhaojl@hshbao.com
 * @Date: 2019-09-19
 * @Version: 1.0
 */
@Slf4j
public class MongoDao {

  private final MongoClient mongoClient;

  private final UpdateOptions options;

  public MongoDao(MongoClient mongoClient){
    this.mongoClient = mongoClient;
    this.options = new UpdateOptions().setMulti(true);
  }

  //有ID就更新文档，没ID就插入文档
  public void save(String collection, JsonObject document, Handler<AsyncResult<String>> resultHandler){
    mongoClient.save(collection, document, resultHandler);
  }

  //插入文档，有ID会报错
  public void insert(String collection, JsonObject document, Handler<AsyncResult<String>> resultHandler){
    mongoClient.insert(collection, document, resultHandler);
  }

  //更新文档
  public void updates(String collection, JsonObject query, JsonObject document, Handler<AsyncResult<MongoClientUpdateResult>> resultHandler){
    JsonObject update = new JsonObject().put("$set", document);
    mongoClient.updateCollectionWithOptions(collection, query, update, options, resultHandler);
  }

  //更换文档
  public void replace(String collection, JsonObject query, JsonObject replace, Handler<AsyncResult<MongoClientUpdateResult>> resultHandler){
    mongoClient.replaceDocuments(collection, query, replace,  resultHandler);
  }

  // 一条语句执行多个UPDATE, REPLACE, INSERT, DELETE; 批量操作
//  public void bulkWrite(String collection, JsonObject query, JsonObject replace){
////    String collection, List<BulkOperation> operations, BulkWriteOptions bulkWriteOptions
//    mongoClient.bulkWriteWithOptions(collection, operations, bulkWriteOptions, res -> {
//      if (res.failed()) {
//        log.error("MongoClient replace error, case by : {}", res.cause().getMessage());
//      }
//    });
//  }

  //只匹配返回的第一条数据
  public void findOne(String collection, JsonObject query, Handler<AsyncResult<JsonObject>> resultHandler){
    mongoClient.findOne(collection, query, null, resultHandler);
  }

  //普通查询
  public void find(String collection, JsonObject query, Handler<AsyncResult<List<JsonObject>>> resultHandler){
    mongoClient.find(collection, query, resultHandler);
  }

  //只排序, 针对小型数据集
  public void findWithSort(String collection, JsonObject query, JsonObject sort, Handler<AsyncResult<List<JsonObject>>> resultHandler){
    FindOptions findOptions = new FindOptions().setSort(sort);
    mongoClient.findWithOptions(collection, query, findOptions, resultHandler);
  }

  //排序且分页, 针对小型数据集
  public void findWithSortAndPage(String collection, JsonObject query, JsonObject sort, int skip, int limit, Handler<AsyncResult<List<JsonObject>>> resultHandler){
    FindOptions findOptions = new FindOptions().setSort(sort).setSkip(skip).setLimit(limit);
    mongoClient.findWithOptions(collection, query, findOptions, resultHandler);
  }

  //查询大型数据集，handler一条一条的接受
  public void findBath(String collection, JsonObject query, Handler<JsonObject> handler){
    FindOptions options = new FindOptions().setBatchSize(100);
    mongoClient.findBatchWithOptions(collection, query, options)
      .exceptionHandler(throwable -> Throwables.getStackTraceAsString(throwable))
      .endHandler(v -> System.out.println("End of research"))
      .handler(handler);
  }

  //删除
  public void remove(String collection, JsonObject query, Handler<AsyncResult<MongoClientDeleteResult>> resultHandler){
    mongoClient.removeDocuments(collection, query, resultHandler);
  }

  //计量
  public void count(String collection, JsonObject query, Handler<AsyncResult<Long>> resultHandler){
    mongoClient.count(collection, query, resultHandler);
  }
}
