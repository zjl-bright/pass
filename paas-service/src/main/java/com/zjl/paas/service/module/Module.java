package com.zjl.paas.service.module;

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
public class Module extends BaseEntity {

    private String name;

    private String gitPath;

    private String dirPath;

    private String cmd;

    private String targetPath;

    private List<Module> children;

//    private String file;
//
//    /**
//     * TODO
//     * @Auther: zjl
//     * @Date: 2019-11-18
//     * @Version: 1.0
//     */
//    @RequestMapping("/project")
//    @Singleton
//    public static class CMDHandler {
//
//
//        @RequestMapping()
//        public void findAll(RoutingContext context){
//
//        }
//    }
}
