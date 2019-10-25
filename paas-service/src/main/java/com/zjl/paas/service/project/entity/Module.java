package com.zjl.paas.service.project.entity;

import com.zjl.paas.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

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

    private String name;

    private String sourcesPath;

    private String cmd;

    private String targetPath;

    private String file;

}