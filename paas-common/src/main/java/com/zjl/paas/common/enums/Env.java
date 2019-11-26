package com.zjl.paas.common.enums;

public enum Env {
    Master("生产", "master"),

    Develop("预发", "develop"),

    Test("测试", "test");

    private String env;

    private String branch;

    Env(String env, String branch){
        this.env = env;
        this.branch = branch;
    }

    public String env(){
        return env;
    }

    public String branch(){
        return branch;
    }
}
