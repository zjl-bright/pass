package com.zjl.paas.service.project.handler;

import com.zjl.paas.common.dao.MongoDao;
import com.zjl.paas.common.dao.RedisDao;

/**
 * TODO
 *
 * @Auther: zhaojl@hshbao.com
 * @Date: 2019-09-21
 * @Version: 1.0
 */
public class ProjectHandler {

  private final MongoDao mongoDao;

  private final RedisDao redisDao;

  public ProjectHandler(MongoDao mongoDao, RedisDao redisDao){
    this.mongoDao = mongoDao;
    this.redisDao = redisDao;
  }


}
