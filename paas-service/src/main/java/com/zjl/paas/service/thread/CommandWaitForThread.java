package com.zjl.paas.service.thread;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-11-29
 * @Version: 1.0
 */
@Slf4j
public class CommandWaitForThread extends Thread{

    private String cmd;
    private String[] args;
    private boolean finish = false;
    private int exitValue = -1;

    public CommandWaitForThread(String cmd) {
        this.cmd = cmd;
    }

    public CommandWaitForThread(String cmd, String[] args) {
        this.cmd = cmd;
        this.args = args;
    }

    @Override
    public void run(){
        try {
            Process process = Runtime.getRuntime().exec(cmd, args);
            //写出脚本执行中的过程信息
            BufferedReader infoInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorInput = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = "";
            while ((line = infoInput.readLine()) != null) {
                log.info(line);
            }
            while ((line = errorInput.readLine()) != null) {
                log.error(line);
            }
            infoInput.close();
            errorInput.close();

            //阻塞执行线程直至脚本执行完成后返回
            this.exitValue = process.waitFor();
        } catch (Throwable e) {
            log.error("CommandWaitForThread failed, cmd：{}, cause by : {}", cmd, Throwables.getStackTraceAsString(e));
            exitValue = 110;
        } finally {
            finish = true;
        }
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public int getExitValue() {
        return exitValue;
    }
}
