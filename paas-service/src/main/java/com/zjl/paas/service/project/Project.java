package com.zjl.paas.service.project;

import com.zjl.paas.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

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
public class Project extends BaseEntity {

    private String name;

    private String dir;

    private String path;

    private List<String> types;

    private String logo;

    private String desc;
}
