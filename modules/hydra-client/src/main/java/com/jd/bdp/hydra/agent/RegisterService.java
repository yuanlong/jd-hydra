package com.jd.bdp.hydra.agent;

import java.util.List;

/**
 * Date: 13-4-3
 * Time: 下午4:00
 * dubbo注册服务接口
 */
public interface RegisterService {
    boolean registerService(List<String> services);

    /*更新注册信息*/
    boolean registerService(String serviceName);
}
