package com.zjl.paas.service.part;

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
public class Part extends BaseEntity {

    private String projectId;

    private String name;

    private String gitPath;

    private String cmd;
}
