package com.jd.bdp.hydra.agent;

import com.jd.bdp.hydra.dubbomonitor.LeaderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: yfliuyu
 * Date: 13-4-7
 * Time: 下午2:27
 */
public class TestLeaderService implements LeaderService{

    @Override
    public Map<String, String> registerClient(List<String> services) {
        return null;
    }

    @Override
    public String registerClient(String service) {
        return null;
    }
}
