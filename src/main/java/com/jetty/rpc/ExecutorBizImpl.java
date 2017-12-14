package com.jetty.rpc;

/**
 * @project 业务实现类
 * @file ExecutorBizImpl.java 创建时间:2017年12月12日下午8:33:20
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class ExecutorBizImpl implements ExecutorBiz {

    @Override
    public String beat() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String idleBeat(int jobId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String kill(int jobId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String run(RequestParam param) {
        System.out.println("run方法执行成功");
        return null;
    }

}
