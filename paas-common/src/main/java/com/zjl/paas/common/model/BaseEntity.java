package com.zjl.paas.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-10-17
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class BaseEntity implements Serializable {

  private String id;

  /**
   * 状态(-1, 0, 1)
   */
  private Integer status;

  /**
   * 创建者
   */
  private String createBy;

  /**
   * 最后一次更新人
   */
  private String updateBy;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;

  /**
   * 最后一次更新时间
   */
  private LocalDateTime updateTime;
}
