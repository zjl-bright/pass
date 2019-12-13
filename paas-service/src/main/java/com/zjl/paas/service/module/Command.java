package com.zjl.paas.service.module;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-12-04
 * @Version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class Command {

    private String partPath;

    private String branchName;

    private String packagePath;

    private String packageCmd;

    private String modulePath;

    private String targetPath;

    private String dockerfilePath;

    private Boolean isDeploy;

    private String ip;
}

