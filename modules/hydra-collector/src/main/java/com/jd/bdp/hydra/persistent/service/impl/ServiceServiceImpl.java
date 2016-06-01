/*
 * Copyright jd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jd.bdp.hydra.persistent.service.impl;

import com.jd.bdp.hydra.persistent.dao.ServiceMapper;
import com.jd.bdp.hydra.persistent.entity.ServicePara;
import com.jd.bdp.hydra.persistent.service.ServiceService;

import java.util.List;

/**
 * User: biandi Date: 13-4-3 Time: 下午1:17
 */
public class ServiceServiceImpl implements ServiceService {
    @Override
    public int getServiceId(String serviceName) {
        ServicePara service = serviceMapper.getService(serviceName);
        if (service == null) {
            service = new ServicePara();
            service.setServiceName(serviceName);
            return serviceMapper.addService(service);
        } else {
            return service.getServiced();
        }
    }

    @Override
    public List<ServicePara> getAllService() {
        return serviceMapper.getAllService();
    }

    private ServiceMapper serviceMapper;

    public void setServiceMapper(ServiceMapper serviceMapper) {
        this.serviceMapper = serviceMapper;
    }
}
