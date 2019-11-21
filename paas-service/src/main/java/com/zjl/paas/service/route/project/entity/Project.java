package com.zjl.paas.service.route.project.entity;

import com.zjl.paas.common.model.BaseEntity;
import io.vertx.codegen.annotations.DataObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @Auther: zhaojl@hshbao.com
 * @Date: 2019-09-21
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
@DataObject
public class Project extends BaseEntity {

  private String name;

  private String logo;

  private String desc;

  private String filePath;

  private String gitPath;

  private List<Map<String, String>> branchs;

  private String cmd;

}
