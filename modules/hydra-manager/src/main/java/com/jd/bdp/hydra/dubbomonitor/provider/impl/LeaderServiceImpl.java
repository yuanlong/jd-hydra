package com.jd.bdp.hydra.dubbomonitor.provider.impl;

import com.jd.bdp.hydra.dubbomonitor.LeaderService;
import com.jd.bdp.hydra.mysql.persistent.service.SeedService;
import com.jd.bdp.hydra.mysql.persistent.service.ServiceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: biandi
 * Date: 13-4-7
 * Time: 下午3:02
 */
public class LeaderServiceImpl implements LeaderService {

    @Override
    public Map<String, String> registerClient(List<String> services) {
        HashMap<String, String> map = new HashMap<>();
        map.put("seed", seedService.getSeed().toString());
        for (String serviceName : services) {
            map.put("serviceName", serviceService.getServiceId(serviceName));
        }
        return map;
    }

    @Override
    public String registerClient(String service) {
        return serviceService.getServiceId(service);
    }

    private ServiceService serviceService;
    private SeedService seedService;

    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    public void setSeedService(SeedService seedService) {
        this.seedService = seedService;
    }

}
