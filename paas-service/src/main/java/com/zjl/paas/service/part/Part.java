package com.zjl.paas.service.part;

import com.zjl.paas.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

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

    private String path;

    private String cmd;

    private List<Env> envs;

    private List<Module> modules;
}
