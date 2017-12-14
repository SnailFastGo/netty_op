package com.jetty.rpc;



/**
 * @project 业务接口
 * @file ExecutorBiz.java 创建时间:2017年12月12日下午8:33:39
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public interface ExecutorBiz {

    /**
     * beat
     * @return
     */
    public String beat();

    /**
     * idle beat
     *
     * @param jobId
     * @return
     */
    public String idleBeat(int jobId);

    /**
     * kill
     * @param jobId
     * @return
     */
    public String kill(int jobId);


    /**
     * run
     * @param 
     * @return
     */
    public String run(RequestParam param);

}
