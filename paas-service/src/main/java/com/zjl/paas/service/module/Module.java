package com.zjl.paas.service.module;

import com.zjl.paas.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-10-24
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class Module extends BaseEntity {

    private String partId;

    private String name;

    private String target;

    private String path;

    private String cmd;
}
