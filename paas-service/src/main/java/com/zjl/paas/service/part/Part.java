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

    private String name;

    private String gitPath;

    private String cmd;

    private List<Module> modules;

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
