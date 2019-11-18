package com.zjl.paas.service.user.entity;

import com.zjl.paas.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
public class User extends BaseEntity {

  private String name;

  private String mobile;

  private String password;
}
