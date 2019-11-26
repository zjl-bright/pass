package com.zjl.paas.service.env;

import com.zjl.paas.common.model.BaseEntity;
import com.zjl.paas.service.module.Module;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-11-25
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class Env extends BaseEntity {

    private String projectId;

    private String name;

    private String branchName;
}
