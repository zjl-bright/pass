package com.zjl.paas.service.util;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * TODO
 *
 * @Auther: zjl
 * @Date: 2019-11-25
 * @Version: 1.0
 */
@Slf4j
@Singleton
public class JGitUtil {

    private UsernamePasswordCredentialsProvider provider;

    @Inject
    public JGitUtil(@Named("git.username")String gitUsername, @Named("git.password")String gitPassword){
        if(StringUtils.isNotBlank(gitUsername) && StringUtils.isNotBlank(gitPassword)){
            provider = new UsernamePasswordCredentialsProvider(gitUsername, gitPassword);
        }
    }

    /**
     * 创建仓库，仅需要执行一次
     */
    public Boolean cloneRepository(String remote_repo_uri, String local_repo_path){
        try {
            if(Objects.isNull(provider)){
                Git.cloneRepository().setURI(remote_repo_uri) //设置远程URI
                        .setBranch("master")   //设置clone下来的分支,默认master
                        .setDirectory(new File(local_repo_path))  //设置下载存放路径
                        .call();
            }else{
                Git.cloneRepository().setURI(remote_repo_uri) //设置远程URI
                        .setBranch("master")   //设置clone下来的分支,默认master
                        .setDirectory(new File(local_repo_path))  //设置下载存放路径
                        .setCredentialsProvider(provider) //设置权限验证
                        .call();
            }
        } catch (Exception e) {
            log.error("Git clone failed by : {}", Throwables.getStackTraceAsString(e));
            return false;
        }
        return true;
    }

    public boolean checkoutAndPull(String local_config, String branchName){
        try(Git git = Git.open( new File(local_config + ".git"))){
            if(branchNameExist(git, branchName)){
                git.checkout().setCreateBranch(false).setName(branchName).call();
            }else{
                git.checkout().setCreateBranch(true).setName(branchName).setStartPoint("origin/" + branchName).call();
            }

            git.pull().setCredentialsProvider(provider).call();
        }catch (Exception e) {
            log.error("JGitUtil checkoutAndPull failed, cause by : {}", Throwables.getStackTraceAsString(e));
            return false;
        }
        return true;
    }

    private boolean branchNameExist(Git git, String branchName) throws GitAPIException {
        List<Ref> refs = git.branchList().call();
        for (Ref ref : refs) {
            if (ref.getName().contains(branchName)) {
                return true;
            }
        }
        return false;
    }
}
